/*
 * Copyright 2006-2008 The Kuali Foundation
 * 
 * Licensed under the Educational Community License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 * http://www.opensource.org/licenses/ecl1.php
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.kuali.kra.timeandmoney.service.impl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.lang.StringUtils;
import org.kuali.kra.award.AwardAmountInfoService;
import org.kuali.kra.award.awardhierarchy.AwardHierarchy;
import org.kuali.kra.award.home.Award;
import org.kuali.kra.award.home.AwardAmountInfo;
import org.kuali.kra.bo.versioning.VersionHistory;
import org.kuali.kra.infrastructure.Constants;
import org.kuali.kra.infrastructure.KraServiceLocator;
import org.kuali.kra.service.VersionHistoryService;
import org.kuali.kra.timeandmoney.AwardHierarchyNode;
import org.kuali.kra.timeandmoney.document.TimeAndMoneyDocument;
import org.kuali.kra.timeandmoney.history.TransactionDetail;
import org.kuali.kra.timeandmoney.service.ActivePendingTransactionsService;
import org.kuali.kra.timeandmoney.transactions.AwardAmountTransaction;
import org.kuali.kra.timeandmoney.transactions.PendingTransaction;
import org.kuali.rice.kns.service.BusinessObjectService;
import org.kuali.rice.kns.util.KualiDecimal;

public class ActivePendingTransactionsServiceImpl implements ActivePendingTransactionsService {
    
    BusinessObjectService businessObjectService;
    AwardAmountInfoService awardAmountInfoService;

    /**
     * 
     * @see org.kuali.kra.timeandmoney.service.ActivePendingTransactionsService#approveTransactions(org.kuali.kra.timeandmoney.document.TimeAndMoneyDocument, org.kuali.kra.timeandmoney.transactions.AwardAmountTransaction)
     */
    public void approveTransactions(TimeAndMoneyDocument doc, AwardAmountTransaction newAwardAmountTransaction) {
        
        Map<String, AwardAmountTransaction> awardAmountTransactionItems = new HashMap<String, AwardAmountTransaction>();
        List<Award> awardItems = new ArrayList<Award>();
        List<TransactionDetail> transactionDetailItems = new ArrayList<TransactionDetail>();        
        List<AwardAmountTransaction> awardAmountTransactions = processTransactions(doc, newAwardAmountTransaction,
                awardAmountTransactionItems, awardItems, transactionDetailItems);
        //deletePendingTransactions
        performSave(doc, transactionDetailItems, awardItems, awardAmountTransactions);
    }

    /**
     * This method...
     * @param doc
     * @param newAwardAmountTransaction
     * @param awardAmountTransactionItems
     * @param awardItems
     * @param transactionDetailItems
     * @param pendingTransactionsToBeDeleted
     * @return
     */
    public List<AwardAmountTransaction> processTransactions(TimeAndMoneyDocument doc,AwardAmountTransaction newAwardAmountTransaction
            , Map<String, AwardAmountTransaction> awardAmountTransactionItems, List<Award> awardItems, List<TransactionDetail> transactionDetailItems) {
        
        List<PendingTransaction> updatedPendingTransactions = new ArrayList<PendingTransaction>();
        List<PendingTransaction> pendingTransactionsToBeDeleted = new ArrayList<PendingTransaction>();
        updatedPendingTransactions.addAll(doc.getPendingTransactions());
        
        for(PendingTransaction pendingTransaction: doc.getPendingTransactions()){
            Map<String, AwardHierarchyNode> awardHierarchyNodes = doc.getAwardHierarchyNodes();
            AwardHierarchyNode sourceAwardNode = awardHierarchyNodes.get(pendingTransaction.getSourceAwardNumber());
            AwardHierarchyNode destinationAwardNode = awardHierarchyNodes.get(pendingTransaction.getDestinationAwardNumber());            
            AwardHierarchyNode parentNode = new AwardHierarchyNode();
            //
            if(StringUtils.equalsIgnoreCase(pendingTransaction.getSourceAwardNumber(),Constants.AWARD_HIERARCHY_DEFAULT_PARENT_OF_ROOT)){
                processPendingTransactionWhenSourceIsExternal(doc, newAwardAmountTransaction, updatedPendingTransactions, transactionDetailItems
                        , awardAmountTransactionItems, awardItems, pendingTransaction, awardHierarchyNodes, destinationAwardNode);   
                
                //
            }else if(StringUtils.equalsIgnoreCase(pendingTransaction.getDestinationAwardNumber(),Constants.AWARD_HIERARCHY_DEFAULT_PARENT_OF_ROOT)){
                processPendingTransactionWhenDestinationIsExternal(doc, newAwardAmountTransaction, updatedPendingTransactions, transactionDetailItems
                        , awardAmountTransactionItems, awardItems, pendingTransaction, awardHierarchyNodes, sourceAwardNode); 
                
                //tests for parent child relationship when pushing money down to children
            }else if(parentChildRelationshipExists(sourceAwardNode.getAwardNumber(), destinationAwardNode.getAwardNumber(), awardHierarchyNodes, parentNode)){                
                processPendingTransactionWhenParentChildRelationShipExists(doc, newAwardAmountTransaction, updatedPendingTransactions, parentNode
                        , transactionDetailItems, awardAmountTransactionItems, awardItems, pendingTransaction, awardHierarchyNodes,
                        sourceAwardNode, destinationAwardNode); 
                
                //tests for child parent relationship when pushing money up to a parent award.
            }else if(childParentRelationshipExists(destinationAwardNode.getAwardNumber(), sourceAwardNode.getAwardNumber(), awardHierarchyNodes, parentNode)){                
                processPendingTransactionWhenChildParentRelationShipExists(doc, newAwardAmountTransaction, updatedPendingTransactions, parentNode
                        , transactionDetailItems, awardAmountTransactionItems, awardItems, pendingTransaction, awardHierarchyNodes,
                        sourceAwardNode, destinationAwardNode); 
            
            }else{processPendingTransactionWithIndirectRelationship(doc, newAwardAmountTransaction, updatedPendingTransactions, transactionDetailItems, awardAmountTransactionItems
                        , awardItems, pendingTransaction, sourceAwardNode, destinationAwardNode);
            }
            
            updatedPendingTransactions.remove(pendingTransaction);
            pendingTransactionsToBeDeleted.add(pendingTransaction);
        }
        
        
        for(Entry<String,AwardHierarchyNode> awardHierarchyNode : doc.getAwardHierarchyNodes().entrySet()){
            Award award = getActiveAwardVersion(awardHierarchyNode.getValue().getAwardNumber());
            AwardAmountInfo awardAmountInfo = awardAmountInfoService.fetchAwardAmountInfoWithHighestTransactionId(award.getAwardAmountInfos());
            awardHierarchyNode.getValue().setCurrentFundEffectiveDate(awardAmountInfo.getCurrentFundEffectiveDate());
            awardHierarchyNode.getValue().setObligationExpirationDate(awardAmountInfo.getObligationExpirationDate());
            awardHierarchyNode.getValue().setFinalExpirationDate(awardAmountInfo.getFinalExpirationDate());
            awardHierarchyNode.getValue().setAnticipatedTotalAmount(awardAmountInfo.getAnticipatedTotalAmount());
            awardHierarchyNode.getValue().setAmountObligatedToDate(awardAmountInfo.getAmountObligatedToDate());
            awardHierarchyNode.getValue().setObliDistributableAmount(awardAmountInfo.getObliDistributableAmount());
            awardHierarchyNode.getValue().setAntDistributableAmount(awardAmountInfo.getAntDistributableAmount());
            //for(Award award: awardItems){
//                if(StringUtils.equalsIgnoreCase(award.getAwardNumber(),awardHierarchyNode.getValue().getAwardNumber())){
//                    awardHierarchyNode.getValue().setCurrentFundEffectiveDate(award.getAwardAmountInfos().get(award.getAwardAmountInfos().size()-1).getCurrentFundEffectiveDate());
//                    awardHierarchyNode.getValue().setObligationExpirationDate(award.getAwardAmountInfos().get(award.getAwardAmountInfos().size()-1).getObligationExpirationDate());
//                    awardHierarchyNode.getValue().setFinalExpirationDate(award.getAwardAmountInfos().get(award.getAwardAmountInfos().size()-1).getFinalExpirationDate());
//                    awardHierarchyNode.getValue().setAnticipatedTotalAmount(award.getAwardAmountInfos().get(award.getAwardAmountInfos().size()-1).getAnticipatedTotalAmount());
//                    awardHierarchyNode.getValue().setAmountObligatedToDate(award.getAwardAmountInfos().get(award.getAwardAmountInfos().size()-1).getAmountObligatedToDate());
//                    awardHierarchyNode.getValue().setObliDistributableAmount(award.getAwardAmountInfos().get(award.getAwardAmountInfos().size()-1).getObliDistributableAmount());
//                    awardHierarchyNode.getValue().setAntDistributableAmount(award.getAwardAmountInfos().get(award.getAwardAmountInfos().size()-1).getAntDistributableAmount());
//
//                }
 //           }
        }
        //this needs to be changed so the pending transactions will stay after doc has been submitted. Probably needs to be done when we
        //call approve transactions.  Between processTransactions and performSave.
        //deletePendingTransactions(doc, pendingTransactionsToBeDeleted);
        
        List<AwardAmountTransaction> awardAmountTransactions = prepareAwardAmountTransactionsListForPersistence(awardAmountTransactionItems);
        return awardAmountTransactions;
    }

    /*
     * This method deletes processed pending transactions from the doc for persistence.
     * 
     * @param doc
     * @param pendingTransactionsToBeDeleted
     */
    private void deletePendingTransactions(TimeAndMoneyDocument doc, List<PendingTransaction> pendingTransactionsToBeDeleted) {
        for(PendingTransaction pendingTransaction: pendingTransactionsToBeDeleted){
            doc.getPendingTransactions().remove(pendingTransaction);
        }
    }

    /*
     * This method prepares a list of AwardAmountTransaction objects for persistence from the map of same objects.
     * @param awardAmountTransactionItems
     * @return
     */
    private List<AwardAmountTransaction> prepareAwardAmountTransactionsListForPersistence(
            Map<String, AwardAmountTransaction> awardAmountTransactionItems) {
        List<AwardAmountTransaction> awardAmountTransactions = new ArrayList<AwardAmountTransaction>();
        for(Entry<String, AwardAmountTransaction> awardAmountTransaction:awardAmountTransactionItems.entrySet()){
            awardAmountTransactions.add(awardAmountTransaction.getValue());
        }
        return awardAmountTransactions;
    }

    /*
     * This is a helper method for save.
     *  
     * @param doc
     * @param transactionDetailItems
     * @param awardItems
     * @param awardAmountTransactions
     */
    private void performSave(TimeAndMoneyDocument doc, List<TransactionDetail> transactionDetailItems, List<Award> awardItems,
            List<AwardAmountTransaction> awardAmountTransactions) {
        businessObjectService.save(transactionDetailItems);
        businessObjectService.save(awardAmountTransactions);
        businessObjectService.save(awardItems);
        businessObjectService.save(doc);
    }

    /*
     * This method processes the pending transaction where there is not a direct path up or down the tree.  The logic will find the common parent
     * and move the money from the source up to this common parent, and then back down to the destination.
     * 
     * @param doc
     * @param newAwardAmountTransaction
     * @param updatedPendingTransactions
     * @param transactionDetailItems
     * @param awardAmountTransactionItems
     * @param awardItems
     * @param pendingTransaction
     * @param sourceAwardNode
     * @param destinationAwardNode
     */
    private void processPendingTransactionWithIndirectRelationship(TimeAndMoneyDocument doc, AwardAmountTransaction newAwardAmountTransaction
            , List<PendingTransaction> updatedPendingTransactions, List<TransactionDetail> transactionDetailItems
            , Map<String, AwardAmountTransaction> awardAmountTransactionItems, List<Award> awardItems, PendingTransaction pendingTransaction
            , AwardHierarchyNode sourceAwardNode, AwardHierarchyNode destinationAwardNode) {
        
        AwardHierarchyNode parentNode = new AwardHierarchyNode();
        //find the common parent of the two nodes in the transaction
        String commonParent = this.findCommonParent(doc.getAwardHierarchyNodes(), sourceAwardNode.getRootAwardNumber(), 
                sourceAwardNode.getAwardNumber(), destinationAwardNode.getAwardNumber());
        //process the transaction by moving the money up the tree from source to common parent.
        processPendingTransactionWhenChildParentRelationShipExistsIndirect(doc, newAwardAmountTransaction, updatedPendingTransactions, parentNode
                , transactionDetailItems, awardAmountTransactionItems, awardItems, pendingTransaction, doc.getAwardHierarchyNodes(),
                sourceAwardNode, doc.getAwardHierarchyNodes().get(commonParent)); 
        //move money from common parent to destination node.
        processPendingTransactionWhenParentChildRelationShipExistsIndirect(doc, newAwardAmountTransaction, updatedPendingTransactions, parentNode
                , transactionDetailItems, awardAmountTransactionItems, awardItems, pendingTransaction, doc.getAwardHierarchyNodes(),
                doc.getAwardHierarchyNodes().get(commonParent), destinationAwardNode); 
    }

    /*
     * This method processes a pending transaction for the case when source is external.
     * @param doc
     * @param newAwardAmountTransaction
     * @param updatedPendingTransactions
     * @param transactionDetailItems
     * @param awardAmountTransactionItems
     * @param awardItems
     * @param pendingTransaction
     * @param awardHierarchyNodes
     * @param destinationAwardNode
     */
    private void processPendingTransactionWhenSourceIsExternal(TimeAndMoneyDocument doc,
            AwardAmountTransaction newAwardAmountTransaction, List<PendingTransaction> updatedPendingTransactions,
            List<TransactionDetail> transactionDetailItems, Map<String, AwardAmountTransaction> awardAmountTransactionItems,
            List<Award> awardItems, PendingTransaction pendingTransaction, Map<String, AwardHierarchyNode> awardHierarchyNodes,
            AwardHierarchyNode destinationAwardNode) {
            //this logic when coming from external source to root Award.
        if(StringUtils.equalsIgnoreCase(pendingTransaction.getDestinationAwardNumber(), destinationAwardNode.getRootAwardNumber())){
            handleDestinationNodeDownTransaction(pendingTransaction, destinationAwardNode.getAwardNumber(), awardAmountTransactionItems, awardItems
                    , updatedPendingTransactions, newAwardAmountTransaction, doc.getDocumentNumber());
            
            addTransactionDetails(Constants.AWARD_HIERARCHY_DEFAULT_PARENT_OF_ROOT,destinationAwardNode.getAwardNumber()
                    ,doc.getAward().getSequenceNumber(), pendingTransaction, doc.getAwardNumber(), doc.getDocumentNumber(), transactionDetailItems);    
        }else{//this logic when moving money from external source to an award other than root award.
            createIntermediateTransactionsWhenParentIsExternal(doc, awardAmountTransactionItems, awardItems, pendingTransaction, destinationAwardNode.getAwardNumber()
                    , awardHierarchyNodes.get(destinationAwardNode.getAwardNumber()).getParentAwardNumber(), Constants.AWARD_HIERARCHY_DEFAULT_PARENT_OF_ROOT, true
                    , transactionDetailItems, updatedPendingTransactions, newAwardAmountTransaction);
            
            handleDestinationNodeDownTransaction(pendingTransaction, destinationAwardNode.getAwardNumber(), awardAmountTransactionItems, awardItems
                    , updatedPendingTransactions, newAwardAmountTransaction, doc.getDocumentNumber());                    
        }
    }
    
   
    
    /*
     * This method processes a pending transaction for the case when source is external.
     * @param doc
     * @param newAwardAmountTransaction
     * @param updatedPendingTransactions
     * @param transactionDetailItems
     * @param awardAmountTransactionItems
     * @param awardItems
     * @param pendingTransaction
     * @param awardHierarchyNodes
     * @param destinationAwardNode
     */
    private void processPendingTransactionWhenDestinationIsExternal(TimeAndMoneyDocument doc,
            AwardAmountTransaction newAwardAmountTransaction, List<PendingTransaction> updatedPendingTransactions,
            List<TransactionDetail> transactionDetailItems, Map<String, AwardAmountTransaction> awardAmountTransactionItems,
            List<Award> awardItems, PendingTransaction pendingTransaction, Map<String, AwardHierarchyNode> awardHierarchyNodes,
            AwardHierarchyNode sourceAwardNode) {
            //this logic when coming from root award to external source.
        if(StringUtils.equalsIgnoreCase(sourceAwardNode.getParentAwardNumber(), Constants.AWARD_HIERARCHY_DEFAULT_PARENT_OF_ROOT)){
            handleSourceNodeUpTransaction(pendingTransaction, sourceAwardNode.getAwardNumber(), awardAmountTransactionItems, awardItems
                    , updatedPendingTransactions, newAwardAmountTransaction, doc.getDocumentNumber());
            
            addTransactionDetails(pendingTransaction.getSourceAwardNumber(),sourceAwardNode.getAwardNumber()
                    ,doc.getAward().getSequenceNumber(), pendingTransaction, doc.getAwardNumber(), doc.getDocumentNumber(), transactionDetailItems);    
        }else{//this logic when moving money from any award other than root award to external source.
            handleSourceNodeUpTransaction(pendingTransaction, sourceAwardNode.getAwardNumber(), awardAmountTransactionItems, awardItems
                    , updatedPendingTransactions, newAwardAmountTransaction, doc.getDocumentNumber()); 
            createIntermediateTransactionsWhenDestinationIsExternal(doc, awardAmountTransactionItems, awardItems, pendingTransaction, sourceAwardNode.getAwardNumber()
                    , awardHierarchyNodes.get(sourceAwardNode.getAwardNumber()).getParentAwardNumber(), Constants.AWARD_HIERARCHY_DEFAULT_PARENT_OF_ROOT, true
                    , transactionDetailItems, updatedPendingTransactions, newAwardAmountTransaction);
                               
        }
    }

    /*
     * This method processes a Pending Transaction for the case when either of the source or destination is parent of the other.
     * @param doc
     * @param newAwardAmountTransaction
     * @param updatedPendingTransactions
     * @param parentNode
     * @param transactionDetailItems
     * @param awardAmountTransactionItems
     * @param awardItems
     * @param pendingTransaction
     * @param awardHierarchyNodes
     * @param sourceAwardNode
     * @param destinationAwardNode
     */
    protected void processPendingTransactionWhenParentChildRelationShipExists(TimeAndMoneyDocument doc, AwardAmountTransaction newAwardAmountTransaction,
            List<PendingTransaction> updatedPendingTransactions, AwardHierarchyNode parentNode,
            List<TransactionDetail> transactionDetailItems, Map<String, AwardAmountTransaction> awardAmountTransactionItems,
            List<Award> awardItems, PendingTransaction pendingTransaction, Map<String, AwardHierarchyNode> awardHierarchyNodes,
            AwardHierarchyNode sourceAwardNode, AwardHierarchyNode destinationAwardNode) {
        
        handleSourceNodeDownTransaction(pendingTransaction, sourceAwardNode.getAwardNumber(), awardAmountTransactionItems, awardItems
        , updatedPendingTransactions, newAwardAmountTransaction, doc.getDocumentNumber());
        
        if(!(destinationAwardNode.getParentAwardNumber().equals(sourceAwardNode.getAwardNumber()))) {
            createIntermediateDownTransactionsWhenParentChildRelationshipExists(doc, awardAmountTransactionItems, awardItems, pendingTransaction, destinationAwardNode.getAwardNumber()
                , awardHierarchyNodes.get(destinationAwardNode.getAwardNumber()).getParentAwardNumber()
                , sourceAwardNode.getAwardNumber(), transactionDetailItems
                , updatedPendingTransactions, newAwardAmountTransaction);
        }
        
        handleDestinationNodeDownTransaction(pendingTransaction, destinationAwardNode.getAwardNumber(), awardAmountTransactionItems, awardItems, updatedPendingTransactions, newAwardAmountTransaction, doc.getDocumentNumber());
    }
    
    /*
     * This method processes a Pending Transaction for the case when either of the source or destination is parent of the other.
     * @param doc
     * @param newAwardAmountTransaction
     * @param updatedPendingTransactions
     * @param parentNode
     * @param transactionDetailItems
     * @param awardAmountTransactionItems
     * @param awardItems
     * @param pendingTransaction
     * @param awardHierarchyNodes
     * @param sourceAwardNode
     * @param destinationAwardNode
     */
    protected void processPendingTransactionWhenParentChildRelationShipExistsIndirect(TimeAndMoneyDocument doc, AwardAmountTransaction newAwardAmountTransaction,
            List<PendingTransaction> updatedPendingTransactions, AwardHierarchyNode parentNode,
            List<TransactionDetail> transactionDetailItems, Map<String, AwardAmountTransaction> awardAmountTransactionItems,
            List<Award> awardItems, PendingTransaction pendingTransaction, Map<String, AwardHierarchyNode> awardHierarchyNodes,
            AwardHierarchyNode sourceAwardNode, AwardHierarchyNode destinationAwardNode) {
       
        if(!(destinationAwardNode.getParentAwardNumber().equals(sourceAwardNode.getAwardNumber()))) {
            createIntermediateDownTransactionsWhenParentChildRelationshipExists(doc, awardAmountTransactionItems, awardItems, pendingTransaction, destinationAwardNode.getAwardNumber()
                , awardHierarchyNodes.get(destinationAwardNode.getAwardNumber()).getParentAwardNumber()
                , sourceAwardNode.getAwardNumber(), transactionDetailItems
                , updatedPendingTransactions, newAwardAmountTransaction);
        }
        
        handleDestinationNodeDownTransaction(pendingTransaction, destinationAwardNode.getAwardNumber(), awardAmountTransactionItems, awardItems, updatedPendingTransactions, newAwardAmountTransaction, doc.getDocumentNumber());
    }
    
    /*
     * This method processes a Pending Transaction for the case when either of the source or destination is parent of the other.
     * @param doc
     * @param newAwardAmountTransaction
     * @param updatedPendingTransactions
     * @param parentNode
     * @param transactionDetailItems
     * @param awardAmountTransactionItems
     * @param awardItems
     * @param pendingTransaction
     * @param awardHierarchyNodes
     * @param sourceAwardNode
     * @param destinationAwardNode
     */
    protected void processPendingTransactionWhenChildParentRelationShipExists(TimeAndMoneyDocument doc, AwardAmountTransaction newAwardAmountTransaction,
            List<PendingTransaction> updatedPendingTransactions, AwardHierarchyNode parentNode,
            List<TransactionDetail> transactionDetailItems, Map<String, AwardAmountTransaction> awardAmountTransactionItems,
            List<Award> awardItems, PendingTransaction pendingTransaction, Map<String, AwardHierarchyNode> awardHierarchyNodes,
            AwardHierarchyNode sourceAwardNode, AwardHierarchyNode destinationAwardNode) {
        
        handleSourceNodeUpTransaction(pendingTransaction, sourceAwardNode.getAwardNumber(), awardAmountTransactionItems, awardItems
        , updatedPendingTransactions, newAwardAmountTransaction, doc.getDocumentNumber());
        
        if(!(sourceAwardNode.getParentAwardNumber().equals(destinationAwardNode.getAwardNumber()))) {
            createIntermediateUpTransactionsWhenParentChildRelationshipExists(doc, awardAmountTransactionItems, awardItems, pendingTransaction, destinationAwardNode.getAwardNumber()
                , awardHierarchyNodes.get(sourceAwardNode.getAwardNumber()).getParentAwardNumber()
                , awardHierarchyNodes.get(sourceAwardNode.getAwardNumber()).getAwardNumber(), transactionDetailItems
                , updatedPendingTransactions, newAwardAmountTransaction);
        }
        
        handleDestinationNodeUpTransaction(pendingTransaction, destinationAwardNode.getAwardNumber(), awardAmountTransactionItems, awardItems, updatedPendingTransactions, newAwardAmountTransaction, doc.getDocumentNumber());
    }
    
    /*
     * This method processes a Pending Transaction for the case when either of the source or destination is parent of the other.
     * @param doc
     * @param newAwardAmountTransaction
     * @param updatedPendingTransactions
     * @param parentNode
     * @param transactionDetailItems
     * @param awardAmountTransactionItems
     * @param awardItems
     * @param pendingTransaction
     * @param awardHierarchyNodes
     * @param sourceAwardNode
     * @param destinationAwardNode
     */
    protected void processPendingTransactionWhenChildParentRelationShipExistsIndirect(TimeAndMoneyDocument doc, AwardAmountTransaction newAwardAmountTransaction,
            List<PendingTransaction> updatedPendingTransactions, AwardHierarchyNode parentNode,
            List<TransactionDetail> transactionDetailItems, Map<String, AwardAmountTransaction> awardAmountTransactionItems,
            List<Award> awardItems, PendingTransaction pendingTransaction, Map<String, AwardHierarchyNode> awardHierarchyNodes,
            AwardHierarchyNode sourceAwardNode, AwardHierarchyNode destinationAwardNode) {
        
        handleSourceNodeUpTransaction(pendingTransaction, sourceAwardNode.getAwardNumber(), awardAmountTransactionItems, awardItems
        , updatedPendingTransactions, newAwardAmountTransaction, doc.getDocumentNumber());
        
        if(!(sourceAwardNode.getParentAwardNumber().equals(destinationAwardNode.getAwardNumber()))) {
            createIntermediateUpTransactionsWhenParentChildRelationshipExists(doc, awardAmountTransactionItems, awardItems, pendingTransaction, destinationAwardNode.getAwardNumber()
                , awardHierarchyNodes.get(sourceAwardNode.getAwardNumber()).getParentAwardNumber()
                , awardHierarchyNodes.get(sourceAwardNode.getAwardNumber()).getAwardNumber(), transactionDetailItems
                , updatedPendingTransactions, newAwardAmountTransaction);
                }
            }

    /*
     * 
     * This method creates the intermediate records of awardAmountInfo and transaction details for case when a parent child relationship exists between 
     * source and destination.
     * 
     * @param doc
     * @param awardAmountTransactionItems
     * @param awardItems
     * @param pendingTransaction
     * @param awardNumber
     * @param parentAwardNumber
     * @param parentOfParentAwardNumber
     * @param direction
     * @param transactionDetailItems
     * @param updatedPendingTransactions
     * @param newAwardAmountTransaction
     */
    protected void createIntermediateDownTransactionsWhenParentChildRelationshipExists(TimeAndMoneyDocument doc, Map<String, AwardAmountTransaction> awardAmountTransactionItems, List<Award> awardItems
            , PendingTransaction pendingTransaction, String destinationAwardNumber, String parentAwardNumber, String sourceAwardNumber
            , List<TransactionDetail> transactionDetailItems, List<PendingTransaction> updatedPendingTransactions
            , AwardAmountTransaction newAwardAmountTransaction) {
                
        while(!StringUtils.equalsIgnoreCase(parentAwardNumber, sourceAwardNumber)){           
            //might not want to keep this in history when creating intermediate transactions.
            addTransactionDetails(sourceAwardNumber,destinationAwardNumber,doc.getAward().getSequenceNumber(), 
                    pendingTransaction, doc.getAwardNumber(), doc.getDocumentNumber(), transactionDetailItems);
            String tempParentAward = parentAwardNumber;
            parentAwardNumber = doc.getAwardHierarchyNodes().get(parentAwardNumber).getParentAwardNumber();            
            handleIntermediateNodeDownTransaction(pendingTransaction, tempParentAward, awardAmountTransactionItems, 
                    awardItems, updatedPendingTransactions, newAwardAmountTransaction, doc.getDocumentNumber());    
           
                        
        }
    }
    
    /*
     * 
     * This method creates the intermediate records of awardAmountInfo and transaction details for case when a parent child relationship exists between 
     * source and destination.
     * 
     * @param doc
     * @param awardAmountTransactionItems
     * @param awardItems
     * @param pendingTransaction
     * @param awardNumber
     * @param parentAwardNumber
     * @param parentOfParentAwardNumber
     * @param direction
     * @param transactionDetailItems
     * @param updatedPendingTransactions
     * @param newAwardAmountTransaction
     */
    protected void createIntermediateUpTransactionsWhenParentChildRelationshipExists(TimeAndMoneyDocument doc, Map<String, AwardAmountTransaction> awardAmountTransactionItems, List<Award> awardItems
            , PendingTransaction pendingTransaction, String destinationAwardNumber, String parentOfSourceAwardNumber, String sourceAwardNumber
            , List<TransactionDetail> transactionDetailItems, List<PendingTransaction> updatedPendingTransactions
            , AwardAmountTransaction newAwardAmountTransaction) {
                
        while(!StringUtils.equalsIgnoreCase(destinationAwardNumber, parentOfSourceAwardNumber)){   
            
            //might not want to keep this in history when creating intermediate transaction.
            addTransactionDetails(sourceAwardNumber,destinationAwardNumber,doc.getAward().getSequenceNumber(), 
                    pendingTransaction, doc.getAwardNumber(), doc.getDocumentNumber(), transactionDetailItems);
            
            handleIntermediateNodeUpTransaction(pendingTransaction, parentOfSourceAwardNumber, awardAmountTransactionItems, 
                    awardItems, updatedPendingTransactions, newAwardAmountTransaction, doc.getDocumentNumber()); 
            sourceAwardNumber = doc.getAwardHierarchyNodes().get(sourceAwardNumber).getParentAwardNumber();
            parentOfSourceAwardNumber = doc.getAwardHierarchyNodes().get(sourceAwardNumber).getParentAwardNumber();                          
        }
    }
    
    /*
     * 
     * This method creates the intermediate records of awardAmountInfo and transaction detail for the case when source is External.
     * @param doc
     * @param awardAmountTransactionItems
     * @param awardItems
     * @param pendingTransaction
     * @param awardNumber
     * @param parentAwardNumber
     * @param parentOfParentAwardNumber
     * @param direction
     * @param transactionDetailItems
     * @param updatedPendingTransactions
     * @param newAwardAmountTransaction
     */
    private void createIntermediateTransactionsWhenParentIsExternal(TimeAndMoneyDocument doc, Map<String, AwardAmountTransaction> awardAmountTransactionItems
            , List<Award> awardItems, PendingTransaction pendingTransaction, String destinationAwardNumber, String parentAwardNumber, String defaultExternalAwardNumber
            , boolean direction, List<TransactionDetail> transactionDetailItems, List<PendingTransaction> updatedPendingTransactions
            , AwardAmountTransaction newAwardAmountTransaction) {
        
        while(!StringUtils.equalsIgnoreCase(parentAwardNumber, Constants.AWARD_HIERARCHY_DEFAULT_PARENT_OF_ROOT)){    
            
            //probably don't need these transaction details added.
            addTransactionDetails(parentAwardNumber,destinationAwardNumber,doc.getAward().getSequenceNumber(), pendingTransaction, doc.getAwardNumber(), doc.getDocumentNumber(), transactionDetailItems);
            handleIntermediateNodeDownTransaction(pendingTransaction, parentAwardNumber, awardAmountTransactionItems, awardItems, updatedPendingTransactions, newAwardAmountTransaction, doc.getDocumentNumber());
            parentAwardNumber = doc.getAwardHierarchyNodes().get(parentAwardNumber).getParentAwardNumber();
            
        }
    }
    
    
    /*
     * 
     * This method creates the intermediate records of awardAmountInfo and transaction detail for the case when source is External.
     * @param doc
     * @param awardAmountTransactionItems
     * @param awardItems
     * @param pendingTransaction
     * @param awardNumber
     * @param parentAwardNumber
     * @param parentOfParentAwardNumber
     * @param direction
     * @param transactionDetailItems
     * @param updatedPendingTransactions
     * @param newAwardAmountTransaction
     */
    private void createIntermediateTransactionsWhenDestinationIsExternal(TimeAndMoneyDocument doc, Map<String, AwardAmountTransaction> awardAmountTransactionItems
            , List<Award> awardItems, PendingTransaction pendingTransaction, String sourceAwardNumber, String parentOfSourceAwardNumber, String destinationAwardNumber
            , boolean direction, List<TransactionDetail> transactionDetailItems, List<PendingTransaction> updatedPendingTransactions
            , AwardAmountTransaction newAwardAmountTransaction) {               
        while(!StringUtils.equalsIgnoreCase(parentOfSourceAwardNumber, Constants.AWARD_HIERARCHY_DEFAULT_PARENT_OF_ROOT)){    
            addTransactionDetails(parentOfSourceAwardNumber,destinationAwardNumber,doc.getAward().getSequenceNumber(), pendingTransaction, doc.getAwardNumber(), doc.getDocumentNumber(), transactionDetailItems);
            handleIntermediateNodeUpTransaction(pendingTransaction, parentOfSourceAwardNumber, awardAmountTransactionItems, awardItems, updatedPendingTransactions, newAwardAmountTransaction, doc.getDocumentNumber());
            parentOfSourceAwardNumber = doc.getAwardHierarchyNodes().get(parentOfSourceAwardNumber).getParentAwardNumber();
        }
    }
    
    /*
     * If there is a parent and child relationship between @awardNumber1 and @awardNumber2. This relationship could be direct or indirect
     * meaning @awardNumber1 could be direct parent or indirect parent of @awardNumber2. Indirect Parent child relationship here means there
     * could be any number of levels between the two. 
     */
    protected boolean parentChildRelationshipExists(String awardNumber1, String awardNumber2, Map<String, AwardHierarchyNode> awardHierarchyNodes, AwardHierarchyNode parentNode) {
        boolean parentChild = Boolean.FALSE;
        String parentAwardNumber = awardHierarchyNodes.get(awardNumber2).getParentAwardNumber();        
        while(!StringUtils.equalsIgnoreCase(parentAwardNumber, Constants.AWARD_HIERARCHY_DEFAULT_PARENT_OF_ROOT)){
            if(StringUtils.equalsIgnoreCase(parentAwardNumber, awardNumber1)){
                parentChild = Boolean.TRUE;
                break;                
            }
            parentAwardNumber = awardHierarchyNodes.get(parentAwardNumber).getParentAwardNumber();            
        }
        
        if(parentChild){
            parentNode.setAwardNumber(awardHierarchyNodes.get(parentAwardNumber).getAwardNumber());             
        }
        
        return parentChild;
    }
    
    /*
     * If there is a child and parent relationship between @awardNumber1 and @awardNumber2. This relationship could be direct or indirect
     * meaning @awardNumber1 could be direct parent or indirect parent of @awardNumber2. Indirect Parent child relationship here means there
     * could be any number of levels between the two. 
     */
    protected boolean childParentRelationshipExists(String awardNumber1, String awardNumber2, Map<String, AwardHierarchyNode> awardHierarchyNodes, AwardHierarchyNode parentNode) {
        boolean parentChild = Boolean.FALSE;
        String parentAwardNumber = awardHierarchyNodes.get(awardNumber2).getParentAwardNumber();        
        while(!StringUtils.equalsIgnoreCase(parentAwardNumber, Constants.AWARD_HIERARCHY_DEFAULT_PARENT_OF_ROOT)){
            if(StringUtils.equalsIgnoreCase(parentAwardNumber, awardNumber1)){
                parentChild = Boolean.TRUE;
                break;                
            }
            parentAwardNumber = awardHierarchyNodes.get(parentAwardNumber).getParentAwardNumber();            
        }
       
        if(parentChild){
            parentNode.setAwardNumber(awardHierarchyNodes.get(parentAwardNumber).getAwardNumber());             
        }
        
        return parentChild;
    }
    
    

    /*
     * This is a helper method that takes care of creating intermediate transactions for the other case when the first two do not apply.
     * 
     * @param doc
     * @param pendingTransaction
     * @param sourceAwardNode
     * @param destinationAwardNode
     * @param updatedPendingTransactions TODO
     * @param newAwardAmountTransaction TODO
     * @param pendingTransactionsToBeDeleted
     */
//    private void createTransaction(TimeAndMoneyDocument doc, PendingTransaction pendingTransaction, AwardHierarchyNode sourceAwardNode
//                , AwardHierarchyNode destinationAwardNode, List<TransactionDetail> transactionDetailItems
//                , Map<String, AwardAmountTransaction> awardAmountTransactionItems, List<Award> awardItems, List<PendingTransaction> updatedPendingTransactions
//                , AwardAmountTransaction newAwardAmountTransaction) {
//        
//        String commonParent = this.findCommonParent(doc.getAwardHierarchyNodes(), sourceAwardNode.getRootAwardNumber(), 
//                sourceAwardNode.getAwardNumber(), destinationAwardNode.getAwardNumber());
//        
//        createTransaction(doc, pendingTransaction, commonParent,sourceAwardNode.getParentAwardNumber(), sourceAwardNode.getAwardNumber(), 
//                true, transactionDetailItems, awardAmountTransactionItems, awardItems, updatedPendingTransactions, 
//                doc.getAwardHierarchyNodes().get(commonParent).getParentAwardNumber(), newAwardAmountTransaction);
//        
//        createTransaction(doc, pendingTransaction, commonParent,destinationAwardNode.getParentAwardNumber(), 
//                destinationAwardNode.getAwardNumber(), false, transactionDetailItems, awardAmountTransactionItems, 
//                awardItems, updatedPendingTransactions, doc.getAwardHierarchyNodes().get(commonParent).getParentAwardNumber(), 
//                newAwardAmountTransaction);
//    }

    /*
     * This is a helper method that takes care of creating intermediate transactions for the other case when the first two do not apply.
     * This method deals with one side at a time based on direction.
     * 
     * @param doc
     * @param pendingTransaction
     * @param commonParent
     * @param parentAwardNumber
     * @param awardNumber TODO
     * @param direction TODO
     * @param updatedPendingTransactions TODO
     * @param parentOfCommonParent TODO
     * @param newAwardAmountTransaction TODO
     * @param pendingTransactionsToBeDeleted
     */
//    protected void createTransaction(TimeAndMoneyDocument doc, PendingTransaction pendingTransaction, String commonParent, String parentAwardNumber
//            , String awardNumber, boolean direction, List<TransactionDetail> transactionDetailItems
//            , Map<String, AwardAmountTransaction> awardAmountTransactionItems, List<Award> awardItems, List<PendingTransaction> updatedPendingTransactions
//            , String parentOfCommonParent, AwardAmountTransaction newAwardAmountTransaction) {
//        
//        String sourceAwardNumber;
//        String destinationAwardNumber;
//        
//        do{ 
//            if(direction){
//                sourceAwardNumber = awardNumber;
//                destinationAwardNumber = parentAwardNumber;
//            }else{
//                sourceAwardNumber = parentAwardNumber;
//                destinationAwardNumber = awardNumber;
//            }
//            
//            addTransactionDetails(sourceAwardNumber,destinationAwardNumber,doc.getAward().getSequenceNumber(), pendingTransaction, doc.getAwardNumber(), doc.getDocumentNumber(), transactionDetailItems);
//            
//            AwardHierarchyNode node = doc.getAwardHierarchyNodes().get(parentAwardNumber);            
//            
//            if(hasCommonParentEntryNotBeenAdded(commonParent, sourceAwardNumber, direction)){
//                handleSingleTransaction(false, false, pendingTransaction, node.getAwardNumber(), awardAmountTransactionItems, awardItems
//                        , updatedPendingTransactions, newAwardAmountTransaction, doc.getDocumentNumber());    
//            }
//             
//            awardNumber = parentAwardNumber;
//            parentAwardNumber = node.getParentAwardNumber();
//        }while(!StringUtils.equalsIgnoreCase(parentOfCommonParent,parentAwardNumber));
//    }

    /*
     * This method is to make sure that there are no double entries made for common parent.
     */
//    private boolean hasCommonParentEntryNotBeenAdded(String commonParent, String sourceAwardNumber, boolean direction) {
//        return direction || !StringUtils.equalsIgnoreCase(sourceAwardNumber, commonParent);
//    }
    
    /*
     * 
     * This method creates a transactionDetail object and adds it to the list for persistence later.
     * 
     * @param sourceAwardNumber
     * @param destinationAwardNumber
     * @param sequenceNumber
     * @param pendingTransaction
     * @param currentAwardNumber
     * @param documentNumber
     * @param transactionDetailItems
     */
    protected void addTransactionDetails(String sourceAwardNumber, String destinationAwardNumber, Integer sequenceNumber, PendingTransaction pendingTransaction, String currentAwardNumber, String documentNumber, List<TransactionDetail> transactionDetailItems){
        TransactionDetail transactionDetail = new TransactionDetail();
        transactionDetail.setSourceAwardNumber(sourceAwardNumber);
        transactionDetail.setSequenceNumber(sequenceNumber);
        transactionDetail.setDestinationAwardNumber(destinationAwardNumber);
        transactionDetail.setAnticipatedAmount(pendingTransaction.getAnticipatedAmount());
        transactionDetail.setObligatedAmount(pendingTransaction.getObligatedAmount());
        transactionDetail.setAwardNumber(currentAwardNumber);
        transactionDetail.setTransactionId(pendingTransaction.getTransactionId());
        transactionDetail.setTimeAndMoneyDocumentNumber(documentNumber);
        transactionDetail.setComments(pendingTransaction.getComments());
        transactionDetailItems.add(transactionDetail);
    }
    
    protected String findCommonParent(Map<String, AwardHierarchyNode> awardHierarchyNodes, String rootAwardNumber, String sourceAwardNumber, String destinationAwardNumber){
        List<String> listOfParentsOfSource = new ArrayList<String>();
        List<String> listOfParentsOfDestination = new ArrayList<String>();
        String commonParent = null;
        
        buildListOfParents(awardHierarchyNodes, listOfParentsOfSource, sourceAwardNumber, rootAwardNumber);
        buildListOfParents(awardHierarchyNodes, listOfParentsOfDestination, destinationAwardNumber, rootAwardNumber);
        
        for(String an1 : listOfParentsOfSource){
            for(String an2 : listOfParentsOfDestination){
                if(StringUtils.equalsIgnoreCase(an1, an2)){
                    commonParent = an1;
                    break;                    
                }
            }
            if(commonParent!=null){
                break;    
            }
        }
        return commonParent;
        
    }

    /**
     * This method...
     * @param awardHierarchyNodes
     * @param listOfParentsOfSource
     * @param currentAwardNumber
     * @return
     */
    private void buildListOfParents(Map<String, AwardHierarchyNode> awardHierarchyNodes, List<String> listOfParentsOfSource,
             String currentAwardNumber, String rootAwardNumber) {
        String parentAwardNumber;
        while(!StringUtils.equalsIgnoreCase(currentAwardNumber,rootAwardNumber)){
            parentAwardNumber = awardHierarchyNodes.get(currentAwardNumber).getParentAwardNumber();
            listOfParentsOfSource.add(parentAwardNumber);
            currentAwardNumber = parentAwardNumber;
        }        
    }
    
    /*
     *
     * This method finds the common parent of source and destination which will be used to create intermediate transactions and validate them.
     * 
     * @param doc
     * @param rootAwardNumber
     * @param parentOfSource
     * @param destinationAwardNumber
     * @return
     */
//    protected String findthisCommonParent(Map<String, AwardHierarchyNode> awardHierarchyNodes, String rootAwardNumber, String sourceAwardNumber, String destinationAwardNumber){
//        String commonParent = rootAwardNumber;
//        
//        while(!StringUtils.equalsIgnoreCase(sourceAwardNumber, rootAwardNumber) || 
//                !StringUtils.equalsIgnoreCase(destinationAwardNumber, rootAwardNumber)) {
//            sourceAwardNumber = awardHierarchyNodes.get(sourceAwardNumber).getParentAwardNumber();
//            destinationAwardNumber = awardHierarchyNodes.get(destinationAwardNumber).getParentAwardNumber();
//            if (StringUtils.equalsIgnoreCase(sourceAwardNumber, destinationAwardNumber)) {
//                commonParent = sourceAwardNumber;
//                break;
//            }
//        }
//        return commonParent;
//    }
    
    /*
    *
    * This method finds the common parent of source and destination which will be used to create intermediate transactions and validate them.
    * 
    * @param doc
    * @param rootAwardNumber
    * @param parentOfSource
    * @param destinationAwardNumber
    * @return
    */
//   protected String findCommonParent(TimeAndMoneyDocument doc, String rootAwardNumber, String parentOfSource, String destinationAwardNumber){
//       boolean commonParentFound = false;        
//       String node = parentOfSource;
//       
//       if(StringUtils.equalsIgnoreCase(parentOfSource, Constants.AWARD_HIERARCHY_DEFAULT_PARENT_OF_ROOT)){
//           node = rootAwardNumber;
//       }else{
//           while(!commonParentFound){
//               List<String> childrenAwardNumbers = findChildren(node);
//               if(childrenAwardNumbers.contains(destinationAwardNumber)){
//                   commonParentFound = true;
//               }else{
//                   node = doc.getAwardHierarchyNodes().get(node).getParentAwardNumber();
//               }
//           }
//       }
//       
//       return node;
//   }

    /*
     * 
     * This method handles one transaction at a time. It gets the current active version of award, retrieves the awardAmountInfo record with 
     * highest transaction id, updates it and also creates awardAmountTransaction object for it.
     * 
     * @param updateAmounts
     * @param addOrSubtract
     * @param pendingTransaction
     * @param awardNumber
     * @param awardAmountTransactionItems
     * @param awardItems
     * @param pendingTransactions
     * @param newAwardAmountTransaction
     * @param documentNumber
     */
//    private void handleSingleTransaction(boolean updateAmounts, boolean addOrSubtract, PendingTransaction pendingTransaction, String awardNumber
//            , Map<String, AwardAmountTransaction> awardAmountTransactionItems, List<Award> awardItems, List<PendingTransaction> pendingTransactions
//            , AwardAmountTransaction newAwardAmountTransaction, String documentNumber) {
//        
//        Award award = getActiveAwardVersion(awardNumber);
//        AwardAmountInfo awardAmountInfo = awardAmountInfoService.fetchAwardAmountInfoWithHighestTransactionId(award.getAwardAmountInfos());
//       // this logic needs to be refactored, maybe need to create separate methods for different ways to handle transactions up or down the tree.
//        
//        if(updateAmounts){
//            if(!addOrSubtract){
//                KualiDecimal totalPendingObligated = new KualiDecimal(0);
//                KualiDecimal totalPendingAnticipated = new KualiDecimal(0);
//                
//                for(PendingTransaction pt : pendingTransactions){
//                    if(pt.getTransactionId()!=null && pendingTransaction.getTransactionId()!= null && !(pt.getTransactionId().longValue() == pendingTransaction.getTransactionId().longValue()) 
//                        && StringUtils.equalsIgnoreCase(pt.getSourceAwardNumber(),pendingTransaction.getSourceAwardNumber())){
//                        totalPendingObligated = totalPendingObligated.add(pt.getObligatedAmount());
//                        totalPendingAnticipated = totalPendingAnticipated.add(pt.getAnticipatedAmount());    
//                    }
//                }
//                
//                validateObliDistributableAmount(pendingTransaction, awardAmountInfo, totalPendingObligated);
//                validateAntiDistributableAmount(pendingTransaction, awardAmountInfo, totalPendingAnticipated);
//            }
//        }
//        award.getAwardAmountInfos().add(getNewAwardAmountInfoEntry(updateAmounts, addOrSubtract, pendingTransaction, awardAmountInfo, awardAmountTransactionItems, newAwardAmountTransaction, documentNumber));
//        awardItems.add(award);
//    }
    
    private void handleDestinationNodeDownTransaction(PendingTransaction pendingTransaction, String awardNumber, 
            Map<String, AwardAmountTransaction> awardAmountTransactionItems, List<Award> awardItems, List<PendingTransaction> pendingTransactions,
            AwardAmountTransaction newAwardAmountTransaction, String documentNumber) {
        
        Award award = getActiveAwardVersion(awardNumber);
        AwardAmountInfo awardAmountInfo = awardAmountInfoService.fetchAwardAmountInfoWithHighestTransactionId(award.getAwardAmountInfos());
        award.getAwardAmountInfos().add(getUpdatedDestinationDownNodeAmountInfo(pendingTransaction, awardAmountInfo, awardAmountTransactionItems, newAwardAmountTransaction, documentNumber));
        awardItems.add(award);
    }
    
    private void handleDestinationNodeUpTransaction(PendingTransaction pendingTransaction, String awardNumber, 
            Map<String, AwardAmountTransaction> awardAmountTransactionItems, List<Award> awardItems, List<PendingTransaction> pendingTransactions,
            AwardAmountTransaction newAwardAmountTransaction, String documentNumber) {
        
        Award award = getActiveAwardVersion(awardNumber);
        AwardAmountInfo awardAmountInfo = awardAmountInfoService.fetchAwardAmountInfoWithHighestTransactionId(award.getAwardAmountInfos());
        award.getAwardAmountInfos().add(getUpdatedDestinationUpNodeAmountInfo(pendingTransaction, awardAmountInfo, awardAmountTransactionItems, newAwardAmountTransaction, documentNumber));
        awardItems.add(award);
    }
    
    /*
     * add money to amount info Totals, and Distributables.
     * 
     */
    private AwardAmountInfo getUpdatedDestinationDownNodeAmountInfo(PendingTransaction pendingTransaction, 
            AwardAmountInfo awardAmountInfo, Map<String, AwardAmountTransaction> awardAmountTransactionItems, 
            AwardAmountTransaction newAwardAmountTransaction, String documentNumber) {
        
        AwardAmountInfo newAwardAmountInfo = new AwardAmountInfo();
        newAwardAmountInfo.setAwardNumber(awardAmountInfo.getAwardNumber());
        newAwardAmountInfo.setSequenceNumber(awardAmountInfo.getSequenceNumber());
        newAwardAmountInfo.setFinalExpirationDate(awardAmountInfo.getFinalExpirationDate());
        newAwardAmountInfo.setCurrentFundEffectiveDate(awardAmountInfo.getCurrentFundEffectiveDate());
        newAwardAmountInfo.setObligationExpirationDate(awardAmountInfo.getObligationExpirationDate());
        newAwardAmountInfo.setTimeAndMoneyDocumentNumber(documentNumber);
        newAwardAmountInfo.setTransactionId(pendingTransaction.getTransactionId());
       //add transaction amounts to the AmountInfo
        newAwardAmountInfo.setObliDistributableAmount(awardAmountInfo.getObliDistributableAmount().add(pendingTransaction.getObligatedAmount()));
        newAwardAmountInfo.setAmountObligatedToDate(awardAmountInfo.getAmountObligatedToDate().add(pendingTransaction.getObligatedAmount()));
        newAwardAmountInfo.setAntDistributableAmount(awardAmountInfo.getAntDistributableAmount().add(pendingTransaction.getAnticipatedAmount()));
        newAwardAmountInfo.setAnticipatedTotalAmount(awardAmountInfo.getAnticipatedTotalAmount().add(pendingTransaction.getAnticipatedAmount()));

        
        //updateAmountFields(updateAmounts, addOrSubtract, pendingTransaction, awardAmountInfo, newAwardAmountInfo);
        
        addAwardAmountTransaction(newAwardAmountInfo.getAwardNumber(), awardAmountTransactionItems, newAwardAmountTransaction, documentNumber);

        return newAwardAmountInfo;
    }
    
    /*
     * add money to amount info Totals, and Distributables.
     * 
     */
    private AwardAmountInfo getUpdatedDestinationUpNodeAmountInfo(PendingTransaction pendingTransaction, 
            AwardAmountInfo awardAmountInfo, Map<String, AwardAmountTransaction> awardAmountTransactionItems, 
            AwardAmountTransaction newAwardAmountTransaction, String documentNumber) {
        
        AwardAmountInfo newAwardAmountInfo = new AwardAmountInfo();
        newAwardAmountInfo.setAwardNumber(awardAmountInfo.getAwardNumber());
        newAwardAmountInfo.setSequenceNumber(awardAmountInfo.getSequenceNumber());
        newAwardAmountInfo.setFinalExpirationDate(awardAmountInfo.getFinalExpirationDate());
        newAwardAmountInfo.setCurrentFundEffectiveDate(awardAmountInfo.getCurrentFundEffectiveDate());
        newAwardAmountInfo.setObligationExpirationDate(awardAmountInfo.getObligationExpirationDate());
        newAwardAmountInfo.setTimeAndMoneyDocumentNumber(documentNumber);
        newAwardAmountInfo.setTransactionId(pendingTransaction.getTransactionId());
       //add transaction amounts to the AmountInfo
        newAwardAmountInfo.setObliDistributableAmount(awardAmountInfo.getObliDistributableAmount().add(pendingTransaction.getObligatedAmount()));
        newAwardAmountInfo.setAmountObligatedToDate(awardAmountInfo.getAmountObligatedToDate());
        newAwardAmountInfo.setAntDistributableAmount(awardAmountInfo.getAntDistributableAmount().add(pendingTransaction.getAnticipatedAmount()));
        newAwardAmountInfo.setAnticipatedTotalAmount(awardAmountInfo.getAnticipatedTotalAmount());

        
        //updateAmountFields(updateAmounts, addOrSubtract, pendingTransaction, awardAmountInfo, newAwardAmountInfo);
        
        addAwardAmountTransaction(newAwardAmountInfo.getAwardNumber(), awardAmountTransactionItems, newAwardAmountTransaction, documentNumber);

        return newAwardAmountInfo;
    }
    
    
    private void handleSourceNodeDownTransaction(PendingTransaction pendingTransaction, String awardNumber, 
            Map<String, AwardAmountTransaction> awardAmountTransactionItems, List<Award> awardItems, List<PendingTransaction> pendingTransactions,
            AwardAmountTransaction newAwardAmountTransaction, String documentNumber) {
        
        Award award = getActiveAwardVersion(awardNumber);
        AwardAmountInfo awardAmountInfo = awardAmountInfoService.fetchAwardAmountInfoWithHighestTransactionId(award.getAwardAmountInfos());
        award.getAwardAmountInfos().add(getUpdatedSourceDownNodeAmountInfo(pendingTransaction, awardAmountInfo, awardAmountTransactionItems, newAwardAmountTransaction, documentNumber));
        awardItems.add(award);
    }
    
    private void handleSourceNodeUpTransaction(PendingTransaction pendingTransaction, String awardNumber, 
            Map<String, AwardAmountTransaction> awardAmountTransactionItems, List<Award> awardItems, List<PendingTransaction> pendingTransactions,
            AwardAmountTransaction newAwardAmountTransaction, String documentNumber) {
        
        Award award = getActiveAwardVersion(awardNumber);
        AwardAmountInfo awardAmountInfo = awardAmountInfoService.fetchAwardAmountInfoWithHighestTransactionId(award.getAwardAmountInfos());
        award.getAwardAmountInfos().add(getUpdatedSourceUpNodeAmountInfo(pendingTransaction, awardAmountInfo, awardAmountTransactionItems, newAwardAmountTransaction, documentNumber));
        awardItems.add(award);
    }
    
    /*
     * add money to amount info Totals, and Distributables.
     * 
     */
    private AwardAmountInfo getUpdatedSourceDownNodeAmountInfo(PendingTransaction pendingTransaction, 
            AwardAmountInfo awardAmountInfo, Map<String, AwardAmountTransaction> awardAmountTransactionItems, 
            AwardAmountTransaction newAwardAmountTransaction, String documentNumber) {
        
        AwardAmountInfo newAwardAmountInfo = new AwardAmountInfo();
        newAwardAmountInfo.setAwardNumber(awardAmountInfo.getAwardNumber());
        newAwardAmountInfo.setSequenceNumber(awardAmountInfo.getSequenceNumber());
        newAwardAmountInfo.setFinalExpirationDate(awardAmountInfo.getFinalExpirationDate());
        newAwardAmountInfo.setCurrentFundEffectiveDate(awardAmountInfo.getCurrentFundEffectiveDate());
        newAwardAmountInfo.setObligationExpirationDate(awardAmountInfo.getObligationExpirationDate());
        newAwardAmountInfo.setTimeAndMoneyDocumentNumber(documentNumber);
        newAwardAmountInfo.setTransactionId(pendingTransaction.getTransactionId());
       //subtract transaction amounts from distributable
        newAwardAmountInfo.setObliDistributableAmount(awardAmountInfo.getObliDistributableAmount().subtract(pendingTransaction.getObligatedAmount()));
        newAwardAmountInfo.setAmountObligatedToDate(awardAmountInfo.getAmountObligatedToDate());
        newAwardAmountInfo.setAntDistributableAmount(awardAmountInfo.getAntDistributableAmount().subtract(pendingTransaction.getAnticipatedAmount()));
        newAwardAmountInfo.setAnticipatedTotalAmount(awardAmountInfo.getAnticipatedTotalAmount());

        
        //updateAmountFields(updateAmounts, addOrSubtract, pendingTransaction, awardAmountInfo, newAwardAmountInfo);
        
        addAwardAmountTransaction(newAwardAmountInfo.getAwardNumber(), awardAmountTransactionItems, newAwardAmountTransaction, documentNumber);

        return newAwardAmountInfo;
    }
    
    /*
     * add money to amount info Totals, and Distributables.
     * 
     */
    private AwardAmountInfo getUpdatedSourceUpNodeAmountInfo(PendingTransaction pendingTransaction, 
            AwardAmountInfo awardAmountInfo, Map<String, AwardAmountTransaction> awardAmountTransactionItems, 
            AwardAmountTransaction newAwardAmountTransaction, String documentNumber) {
        
        AwardAmountInfo newAwardAmountInfo = new AwardAmountInfo();
        newAwardAmountInfo.setAwardNumber(awardAmountInfo.getAwardNumber());
        newAwardAmountInfo.setSequenceNumber(awardAmountInfo.getSequenceNumber());
        newAwardAmountInfo.setFinalExpirationDate(awardAmountInfo.getFinalExpirationDate());
        newAwardAmountInfo.setCurrentFundEffectiveDate(awardAmountInfo.getCurrentFundEffectiveDate());
        newAwardAmountInfo.setObligationExpirationDate(awardAmountInfo.getObligationExpirationDate());
        newAwardAmountInfo.setTimeAndMoneyDocumentNumber(documentNumber);
        newAwardAmountInfo.setTransactionId(pendingTransaction.getTransactionId());
       //subtract transaction amounts from distributable
        newAwardAmountInfo.setObliDistributableAmount(awardAmountInfo.getObliDistributableAmount().subtract(pendingTransaction.getObligatedAmount()));
        newAwardAmountInfo.setAmountObligatedToDate(awardAmountInfo.getAmountObligatedToDate().subtract(pendingTransaction.getObligatedAmount()));
        newAwardAmountInfo.setAntDistributableAmount(awardAmountInfo.getAntDistributableAmount().subtract(pendingTransaction.getAnticipatedAmount()));
        newAwardAmountInfo.setAnticipatedTotalAmount(awardAmountInfo.getAnticipatedTotalAmount().subtract(pendingTransaction.getAnticipatedAmount()));

        
        //updateAmountFields(updateAmounts, addOrSubtract, pendingTransaction, awardAmountInfo, newAwardAmountInfo);
        
        addAwardAmountTransaction(newAwardAmountInfo.getAwardNumber(), awardAmountTransactionItems, newAwardAmountTransaction, documentNumber);

        return newAwardAmountInfo;
    }
    
    
    private void handleIntermediateNodeDownTransaction(PendingTransaction pendingTransaction, String awardNumber, 
            Map<String, AwardAmountTransaction> awardAmountTransactionItems, List<Award> awardItems, List<PendingTransaction> pendingTransactions,
            AwardAmountTransaction newAwardAmountTransaction, String documentNumber) {
        
        Award award = getActiveAwardVersion(awardNumber);
        AwardAmountInfo awardAmountInfo = awardAmountInfoService.fetchAwardAmountInfoWithHighestTransactionId(award.getAwardAmountInfos());
        award.getAwardAmountInfos().add(getUpdatedIntermediateDownNodeAmountInfo(pendingTransaction, awardAmountInfo, awardAmountTransactionItems, newAwardAmountTransaction, documentNumber));
        awardItems.add(award);
    }
    
    private void handleIntermediateNodeUpTransaction(PendingTransaction pendingTransaction, String awardNumber, 
            Map<String, AwardAmountTransaction> awardAmountTransactionItems, List<Award> awardItems, List<PendingTransaction> pendingTransactions,
            AwardAmountTransaction newAwardAmountTransaction, String documentNumber) {
        
        Award award = getActiveAwardVersion(awardNumber);
        AwardAmountInfo awardAmountInfo = awardAmountInfoService.fetchAwardAmountInfoWithHighestTransactionId(award.getAwardAmountInfos());
        award.getAwardAmountInfos().add(getUpdatedIntermediateUpNodeAmountInfo(pendingTransaction, awardAmountInfo, awardAmountTransactionItems, newAwardAmountTransaction, documentNumber));
        awardItems.add(award);
    }
    
    /*
     * add money to amount info Totals, and Distributables.
     * 
     */
    private AwardAmountInfo getUpdatedIntermediateDownNodeAmountInfo(PendingTransaction pendingTransaction, 
            AwardAmountInfo awardAmountInfo, Map<String, AwardAmountTransaction> awardAmountTransactionItems, 
            AwardAmountTransaction newAwardAmountTransaction, String documentNumber) {
        
        AwardAmountInfo newAwardAmountInfo = new AwardAmountInfo();
        newAwardAmountInfo.setAwardNumber(awardAmountInfo.getAwardNumber());
        newAwardAmountInfo.setSequenceNumber(awardAmountInfo.getSequenceNumber());
        newAwardAmountInfo.setFinalExpirationDate(awardAmountInfo.getFinalExpirationDate());
        newAwardAmountInfo.setCurrentFundEffectiveDate(awardAmountInfo.getCurrentFundEffectiveDate());
        newAwardAmountInfo.setObligationExpirationDate(awardAmountInfo.getObligationExpirationDate());
        newAwardAmountInfo.setTimeAndMoneyDocumentNumber(documentNumber);
        newAwardAmountInfo.setTransactionId(pendingTransaction.getTransactionId());
       //add transaction amounts to the AmountInfo
        newAwardAmountInfo.setObliDistributableAmount(awardAmountInfo.getObliDistributableAmount());
        newAwardAmountInfo.setAmountObligatedToDate(awardAmountInfo.getAmountObligatedToDate().add(pendingTransaction.getObligatedAmount()));
        newAwardAmountInfo.setAntDistributableAmount(awardAmountInfo.getAntDistributableAmount());
        newAwardAmountInfo.setAnticipatedTotalAmount(awardAmountInfo.getAnticipatedTotalAmount().add(pendingTransaction.getAnticipatedAmount()));
        
        addAwardAmountTransaction(newAwardAmountInfo.getAwardNumber(), awardAmountTransactionItems, newAwardAmountTransaction, documentNumber);

        return newAwardAmountInfo;
    }
    
    /*
     * add money to amount info Totals, and Distributables.
     * 
     */
    private AwardAmountInfo getUpdatedIntermediateUpNodeAmountInfo(PendingTransaction pendingTransaction, 
            AwardAmountInfo awardAmountInfo, Map<String, AwardAmountTransaction> awardAmountTransactionItems, 
            AwardAmountTransaction newAwardAmountTransaction, String documentNumber) {
        
        AwardAmountInfo newAwardAmountInfo = new AwardAmountInfo();
        newAwardAmountInfo.setAwardNumber(awardAmountInfo.getAwardNumber());
        newAwardAmountInfo.setSequenceNumber(awardAmountInfo.getSequenceNumber());
        newAwardAmountInfo.setFinalExpirationDate(awardAmountInfo.getFinalExpirationDate());
        newAwardAmountInfo.setCurrentFundEffectiveDate(awardAmountInfo.getCurrentFundEffectiveDate());
        newAwardAmountInfo.setObligationExpirationDate(awardAmountInfo.getObligationExpirationDate());
        newAwardAmountInfo.setTimeAndMoneyDocumentNumber(documentNumber);
        newAwardAmountInfo.setTransactionId(pendingTransaction.getTransactionId());
       //add transaction amounts to the AmountInfo
        newAwardAmountInfo.setObliDistributableAmount(awardAmountInfo.getObliDistributableAmount());
        newAwardAmountInfo.setAmountObligatedToDate(awardAmountInfo.getAmountObligatedToDate().subtract(pendingTransaction.getObligatedAmount()));
        newAwardAmountInfo.setAntDistributableAmount(awardAmountInfo.getAntDistributableAmount());
        newAwardAmountInfo.setAnticipatedTotalAmount(awardAmountInfo.getAnticipatedTotalAmount().subtract(pendingTransaction.getAnticipatedAmount()));
        
        addAwardAmountTransaction(newAwardAmountInfo.getAwardNumber(), awardAmountTransactionItems, newAwardAmountTransaction, documentNumber);

        return newAwardAmountInfo;
    }

    /*
     * This method validates the AntiDistributableAmount
     * @param pendingTransaction
     * @param awardAmountInfo
     * @param totalPendingAnticipated
     */
    protected void validateAntiDistributableAmount(PendingTransaction pendingTransaction, AwardAmountInfo awardAmountInfo, KualiDecimal totalPendingAnticipated){
        if(pendingTransaction.getAnticipatedAmount()!=null){
            if(awardAmountInfo.getAntDistributableAmount().subtract(pendingTransaction.getAnticipatedAmount().add(totalPendingAnticipated)).isLessThan(new KualiDecimal(0))){
                throw new RuntimeException("Insufficient Anticipated Money");
            }
        }
    }

    /*
     * This method validates the ObliDistributableAmount
     * @param pendingTransaction
     * @param awardAmountInfo
     * @param totalPendingObligated
     */
    protected void validateObliDistributableAmount(PendingTransaction pendingTransaction, AwardAmountInfo awardAmountInfo, KualiDecimal totalPendingObligated) {
        if(pendingTransaction.getObligatedAmount()!=null){
            if(awardAmountInfo.getObliDistributableAmount().subtract(pendingTransaction.getObligatedAmount().add(totalPendingObligated)).isLessThan(new KualiDecimal(0))){
                throw new RuntimeException("Insufficient Obligated Money");
            }
        }
    }
   
    
//    private List<String> findChildren(String parent) {
//        Map<String, String> fieldValues = new HashMap<String, String>();
//        fieldValues.put("parentAwardNumber", parent);
//        Collection<AwardHierarchy> awardHierarchies = businessObjectService.findMatchingOrderBy(AwardHierarchy.class, fieldValues, "awardNumber", true);
//        Collection<AwardHierarchy> awardHierarchiesBig = businessObjectService.findMatchingOrderBy(AwardHierarchy.class, fieldValues, "awardNumber", true);
//        List<String> parentAwardNumbers = new ArrayList<String>();
//        
//        for(AwardHierarchy awardHierarchy: awardHierarchies){
//            fieldValues.put("parentAwardNumber", awardHierarchy.getAwardNumber());
//            awardHierarchiesBig.addAll(businessObjectService.findMatchingOrderBy(AwardHierarchy.class, fieldValues, "awardNumber", true));                
//        }
//        
//        for(AwardHierarchy awardHierarchy: awardHierarchiesBig){
//            parentAwardNumbers.add(awardHierarchy.getAwardNumber());    
//        }
//        
//        return parentAwardNumbers; 
//    }

    /*
     * This method will create a newAwardAmountInfo object based on the existing awardAmountInfo object.
     * It will also update various amount fields based on various conditions.
     * 
     */
//    private AwardAmountInfo getNewAwardAmountInfoEntry(boolean updateAmounts, boolean addOrSubtract, PendingTransaction pendingTransaction
//            , AwardAmountInfo awardAmountInfo, Map<String, AwardAmountTransaction> awardAmountTransactionItems, AwardAmountTransaction newAwardAmountTransaction, String documentNumber) {
//        AwardAmountInfo newAwardAmountInfo = new AwardAmountInfo();
//        newAwardAmountInfo.setAwardNumber(awardAmountInfo.getAwardNumber());
//        newAwardAmountInfo.setSequenceNumber(awardAmountInfo.getSequenceNumber());
//        newAwardAmountInfo.setFinalExpirationDate(awardAmountInfo.getFinalExpirationDate());
//        newAwardAmountInfo.setCurrentFundEffectiveDate(awardAmountInfo.getCurrentFundEffectiveDate());
//        newAwardAmountInfo.setObligationExpirationDate(awardAmountInfo.getObligationExpirationDate());
//        newAwardAmountInfo.setTimeAndMoneyDocumentNumber(documentNumber);
//        newAwardAmountInfo.setTransactionId(pendingTransaction.getTransactionId());
//        
//        updateAmountFields(updateAmounts, addOrSubtract, pendingTransaction, awardAmountInfo, newAwardAmountInfo);
//        
//        addAwardAmountTransaction(newAwardAmountInfo.getAwardNumber(), awardAmountTransactionItems, newAwardAmountTransaction, documentNumber);
//
//        return newAwardAmountInfo;
//    }

    /*
     * This method will update the amount fields on newAwardAmountInfo object based on various conditions.
     * 
     */
    protected void updateAmountFields(boolean updateAmounts, boolean addOrSubtract, PendingTransaction pendingTransaction, AwardAmountInfo awardAmountInfo
            , AwardAmountInfo newAwardAmountInfo) {

        newAwardAmountInfo.setObliDistributableAmount(processAmounts(awardAmountInfo.getObliDistributableAmount(), pendingTransaction.getObligatedAmount(),addOrSubtract, updateAmounts));            
        newAwardAmountInfo.setAntDistributableAmount(processAmounts(awardAmountInfo.getAntDistributableAmount(), pendingTransaction.getAnticipatedAmount(),addOrSubtract, updateAmounts));
        
        if(StringUtils.equalsIgnoreCase(pendingTransaction.getSourceAwardNumber(), Constants.AWARD_HIERARCHY_DEFAULT_PARENT_OF_ROOT)){
            newAwardAmountInfo.setAnticipatedTotalAmount(processAmounts(awardAmountInfo.getAnticipatedTotalAmount(), pendingTransaction.getAnticipatedAmount(),true, true));
            newAwardAmountInfo.setAmountObligatedToDate(processAmounts(awardAmountInfo.getAmountObligatedToDate(), pendingTransaction.getObligatedAmount(),true, true));
        }
        else if(addOrSubtract){            
            newAwardAmountInfo.setAnticipatedTotalAmount(processAmounts(awardAmountInfo.getAnticipatedTotalAmount(), pendingTransaction.getAnticipatedAmount(),addOrSubtract, updateAmounts));
            newAwardAmountInfo.setAmountObligatedToDate(processAmounts(awardAmountInfo.getAmountObligatedToDate(), pendingTransaction.getObligatedAmount(),addOrSubtract, updateAmounts));
        }else{            
            newAwardAmountInfo.setAnticipatedTotalAmount(awardAmountInfo.getAnticipatedTotalAmount());
            newAwardAmountInfo.setAmountObligatedToDate(awardAmountInfo.getAmountObligatedToDate());
        }
        
        if(updateAmounts){
            newAwardAmountInfo.setObligatedChange(pendingTransaction.getObligatedAmount());
            newAwardAmountInfo.setAnticipatedChange(pendingTransaction.getAnticipatedAmount());    
        }else if(StringUtils.equalsIgnoreCase(pendingTransaction.getSourceAwardNumber(), Constants.AWARD_HIERARCHY_DEFAULT_PARENT_OF_ROOT)){
            newAwardAmountInfo.setObligatedChange(pendingTransaction.getObligatedAmount());
            newAwardAmountInfo.setAnticipatedChange(pendingTransaction.getAnticipatedAmount());
        }else{
            newAwardAmountInfo.setObligatedChange(new KualiDecimal(0));
            newAwardAmountInfo.setAnticipatedChange(new KualiDecimal(0));
        }
    }
    
    /*
     * This is a helper method to add awardAmountTransaction information.
     * 
     * AwardAmountTransacion table is going to have one entry per document, per affected award.
     * Affected award here means an award that is part of any of the Pending Transactions.
     * 
     * That's why we will maintain a map of AwardAmountTransaction Objects with awardNumber as the key. If the key is present, we won't
     * add a new entry here, otherwise we will. We will persist all awardAmountTransaction objects later.
     */
    private void addAwardAmountTransaction(String awardNumber, Map<String, AwardAmountTransaction> awardAmountTransactionItems, AwardAmountTransaction newAwardAmountTransaction, String documentNumber) {
        if(!awardAmountTransactionItems.containsKey(awardNumber)){
            AwardAmountTransaction newAwardAmountTransaction1 = new AwardAmountTransaction(); 
            newAwardAmountTransaction1.setAwardNumber(awardNumber);
            newAwardAmountTransaction1.setDocumentNumber(documentNumber);
            newAwardAmountTransaction1.setTransactionTypeCode(newAwardAmountTransaction.getTransactionTypeCode());
            newAwardAmountTransaction1.setComments(newAwardAmountTransaction.getComments());
            newAwardAmountTransaction1.setNoticeDate(newAwardAmountTransaction.getNoticeDate());
            awardAmountTransactionItems.put(awardNumber, newAwardAmountTransaction1);    
        }       
    }

    /*
     * 
     * This is a helper method to carry out calculation on various amount fields.
     * 
     * updateAmounts true means the calculations should be done otherwise the same value is returned.
     * addOrSubtract true means the value should be added and false means value should be subtracted.
     * 
     */
    protected KualiDecimal processAmounts(KualiDecimal value1, KualiDecimal value2, boolean addOrSubtract, boolean updateAmounts){
        KualiDecimal returnValue;
        if(updateAmounts){
            if(addOrSubtract){
                if(value1!=null){
                    returnValue =  value1.add(value2);    
                }else{
                    returnValue = value2;
                }   
            }else{
                if(value1!=null){
                    returnValue =  value1.subtract(value2);    
                }else{
                    returnValue = value2;
                }
            }
        }else{
            returnValue = value1;
        }
        return returnValue;
    }
    
    /**
     * 
     * @see org.kuali.kra.timeandmoney.service.ActivePendingTransactionsService#getActiveAwardVersion(java.lang.String)
     */
    public Award getActiveAwardVersion(String awardNumber) {
        VersionHistoryService vhs = KraServiceLocator.getService(VersionHistoryService.class);  
        VersionHistory vh = vhs.findActiveVersion(Award.class, awardNumber);
        Award award = null;
        
        if(vh!=null){
            award = (Award) vh.getSequenceOwner();
        }else{
            BusinessObjectService businessObjectService =  KraServiceLocator.getService(BusinessObjectService.class);
            award = ((List<Award>)businessObjectService.findMatching(Award.class, getHashMap(awardNumber))).get(0);              
        }
        return award;
    }

    private Map<String, String> getHashMap(String goToAwardNumber) {
        Map<String, String> map = new HashMap<String,String>();
        map.put("awardNumber", goToAwardNumber);
        return map;
    }
    
    /**
     * Gets the businessObjectService attribute. 
     * @return Returns the businessObjectService.
     */
    public BusinessObjectService getBusinessObjectService() {
        return businessObjectService;
    }

    /**
     * Sets the businessObjectService attribute value.
     * @param businessObjectService The businessObjectService to set.
     */
    public void setBusinessObjectService(BusinessObjectService businessObjectService) {
        this.businessObjectService = businessObjectService;
    }

    /**
     * Gets the awardAmountInfoService attribute. 
     * @return Returns the awardAmountInfoService.
     */
    public AwardAmountInfoService getAwardAmountInfoService() {
        return awardAmountInfoService;
    }

    /**
     * Sets the awardAmountInfoService attribute value.
     * @param awardAmountInfoService The awardAmountInfoService to set.
     */
    public void setAwardAmountInfoService(AwardAmountInfoService awardAmountInfoService) {
        this.awardAmountInfoService = awardAmountInfoService;
    }

}
