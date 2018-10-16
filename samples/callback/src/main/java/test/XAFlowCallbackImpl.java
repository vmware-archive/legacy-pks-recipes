package com.ibm.ws.Transaction.test.impl;

import com.ibm.ws.Transaction.test.XAFlowCallback;
import java.io.PrintStream;

public class XAFlowCallbackImpl
  implements XAFlowCallback
{
  public static void initialize()
  {
  }

  public static final boolean isEnabled()
  {
    return true;
  }

  public boolean beforeXAFlow(int flowType, int flag)
  {
    System.out.println("XAFLOWCALLBACK: Before flow type: " + getFlowType(flowType) + ", Before flag: " + getBeforeFlag(flag));
    if (flowType == 2)
    {
      System.out.println("XAFLOWCALLBACK: Bring down server");
      Runtime.getRuntime().halt(1);
    }
    return true;
  }

  public boolean afterXAFlow(int flowType, int flag)
  {
    System.out.println("XAFLOWCALLBACK: After flow type: " + getFlowType(flowType) + ", After flag: " + getAfterFlag(flag));
    return true;
  }

  private String getFlowType(int flowType)
  {
    String strFlowType = "";

    switch (flowType)
    {
    case 1:
      strFlowType = "PREPARE";
      break;
    case 2:
      strFlowType = "COMMIT";
      break;
    case 3:
      strFlowType = "ROLLBACK";
      break;
    case 0:
    default:
      strFlowType = "FORGET";
    }

    return strFlowType;
  }

  private String getBeforeFlag(int flag)
  {
    String strFlag = "";

    switch (flag)
    {
    case 10:
      strFlag = "FORGET_NORMAL";
      break;
    case 20:
      strFlag = "PREPARE_NORMAL";
      break;
    case 21:
      strFlag = "PREPARE_1PC_OPT";
      break;
    case 30:
      strFlag = "COMMIT_2PC";
      break;
    case 31:
      strFlag = "COMMIT_1PC_OPT";
      break;
    case 40:
      strFlag = "ROLLBACK_NORMAL";
      break;
    case 41:
      strFlag = "ROLLBACK_DUE_TO_ERROR";
      break;
    default:
      strFlag = "UNEXPECTED FLAG " + flag;
    }

    return strFlag;
  }

  private String getAfterFlag(int flag)
  {
    String strFlag = "";

    switch (flag)
    {
    case 50:
      strFlag = "SUCCESS";
      break;
    case 51:
      strFlag = "FAIL";
      break;
    default:
      strFlag = "UNEXPECTED FLAG " + flag;
    }

    return strFlag;
  }
}
