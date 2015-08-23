
/**
 * MyServiceSearchCallbackHandler.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis2 version: 1.5.3  Built on : Nov 12, 2010 (02:24:07 CET)
 */

    package pos.mysoap.stub;

    /**
     *  MyServiceSearchCallbackHandler Callback class, Users can extend this class and implement
     *  their own receiveResult and receiveError methods.
     */
    public abstract class MyServiceSearchCallbackHandler{



    protected Object clientData;

    /**
    * User can pass in any object that needs to be accessed once the NonBlocking
    * Web service call is finished and appropriate method of this CallBack is called.
    * @param clientData Object mechanism by which the user can pass in user data
    * that will be avilable at the time this callback is called.
    */
    public MyServiceSearchCallbackHandler(Object clientData){
        this.clientData = clientData;
    }

    /**
    * Please use this constructor if you don't want to set any clientData
    */
    public MyServiceSearchCallbackHandler(){
        this.clientData = null;
    }

    /**
     * Get the client data
     */

     public Object getClientData() {
        return clientData;
     }

        
           /**
            * auto generated Axis2 call back method for dosearch method
            * override this method for handling normal response from dosearch operation
            */
           public void receiveResultdosearch(
                    pos.mysoap.stub.MyServiceSearchStub.DosearchResponse result
                        ) {
           }

          /**
           * auto generated Axis2 Error handler
           * override this method for handling error response from dosearch operation
           */
            public void receiveErrordosearch(java.lang.Exception e) {
            }
                


    }
    