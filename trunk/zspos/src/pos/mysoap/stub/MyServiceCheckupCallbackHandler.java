
/**
 * MyServiceCheckupCallbackHandler.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis2 version: 1.5.3  Built on : Nov 12, 2010 (02:24:07 CET)
 */

    package pos.mysoap.stub;

    /**
     *  MyServiceCheckupCallbackHandler Callback class, Users can extend this class and implement
     *  their own receiveResult and receiveError methods.
     */
    public abstract class MyServiceCheckupCallbackHandler{



    protected Object clientData;

    /**
    * User can pass in any object that needs to be accessed once the NonBlocking
    * Web service call is finished and appropriate method of this CallBack is called.
    * @param clientData Object mechanism by which the user can pass in user data
    * that will be avilable at the time this callback is called.
    */
    public MyServiceCheckupCallbackHandler(Object clientData){
        this.clientData = clientData;
    }

    /**
    * Please use this constructor if you don't want to set any clientData
    */
    public MyServiceCheckupCallbackHandler(){
        this.clientData = null;
    }

    /**
     * Get the client data
     */

     public Object getClientData() {
        return clientData;
     }

        
           /**
            * auto generated Axis2 call back method for docheck method
            * override this method for handling normal response from docheck operation
            */
           public void receiveResultdocheck(
                    pos.mysoap.stub.MyServiceCheckupStub.DocheckResponse result
                        ) {
           }

          /**
           * auto generated Axis2 Error handler
           * override this method for handling error response from docheck operation
           */
            public void receiveErrordocheck(java.lang.Exception e) {
            }
                


    }
    