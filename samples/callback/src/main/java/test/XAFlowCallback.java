package com.ibm.ws.Transaction.test;
/* ************************************************************************** */
/* COMPONENT_NAME: WAS.transactions                                           */
/*                                                                            */
/*  ORIGINS: 27                                                               */
/*                                                                            */
/* IBM Confidential OCO Source Material                                       */
/* 5630-A36. (C) COPYRIGHT International Business Machines Corp. 2003         */
/* The source code for this program is not published or otherwise divested    */
/* of its trade secrets, irrespective of what has been deposited with the     */
/* U.S. Copyright Office.                                                     */
/*                                                                            */
/* %Z% %I% %W% %G% %U% [%H% %T%]                                              */
/*                                                                            */
/*  DESCRIPTION:                                                              */
/*                                                                            */
/*  Change History:                                                           */
/*                                                                            */
/*  Date      Programmer    Defect    Description                             */
/*  --------  ----------    ------    -----------                             */
/*  24/02/03    gareth      159659     Creation                               */
/* ************************************************************************** */

public interface XAFlowCallback
{
    // Flow type definitions
    public final static int FORGET   = 0;
    public final static int PREPARE  = 1;
    public final static int COMMIT   = 2;
    public final static int ROLLBACK = 3;

    //Before flag definitions
    public final static int FORGET_NORMAL         = 10;

    public final static int PREPARE_NORMAL        = 20;
    public final static int PREPARE_1PC_OPT       = 21;

    public final static int COMMIT_2PC            = 30;
    public final static int COMMIT_1PC_OPT        = 31;

    public final static int ROLLBACK_NORMAL       = 40;
    public final static int ROLLBACK_DUE_TO_ERROR = 41;

    //After flag definitions
    public final static int AFTER_SUCCESS         = 50;
    public final static int AFTER_FAIL            = 51;


    /**
     * Called before the current resource is flowed the XA signal
     * defined in flowType.
     *
     * @param flowType The current signal type:<br>
     *
     *                 <UL>
     *                 <LI>FORGET</LI>
     *                 <LI>PREPARE</LI>
     *                 <LI>COMMIT</LI>
     *                 <LI>ROLLBACK</LI>
     *                 </UL>
     * @param flag     A call specific flag that provides more detail about the type of call about
     *                 to be made to the resource:
     *
     *                 <UL>
     *                 <LI>FORGET_NORMAL</LI>
     *                 <LI>PREPARE_NORMAL</LI>
     *                 <LI>PREPARE_1PC_OPT</LI>
     *                 <LI>COMMIT_2PC</LI>
     *                 <LI>COMMIT_1PC_OPT</LI>
     *                 <LI>ROLLBACK_NORMAL</LI>
     *                 <LI>ROLLBACK_DUE_TO_ERROR</LI>
     *                 </UL>
     *
     * @return True - Current resource is flowed the current signal type<br>
     *         False - Current resource is skipped
     */
    public boolean beforeXAFlow(int flowType, int flag);

    /**
     * Called before the current resource has been flowed the XA signal
     * defined in flowType.
     *
     * @param flowType The current signal type:<br>
     *
     *                 <UL>
     *                 <LI>FORGET</LI>
     *                 <LI>PREPARE</LI>
     *                 <LI>COMMIT</LI>
     *                 <LI>ROLLBACK</LI>
     *                 </UL>
     * @param flag     Flag specifying whether or not the call to the resource succeded:
     *
     *                 <UL>
     *                 <LI>AFTER_SUCCESS</LI>
     *                 <LI>AFTER_FAIL</LI>
     *                 </UL>
     *
     * @return Not currently used.
     */
    public boolean afterXAFlow(int flowType, int flag);
}
