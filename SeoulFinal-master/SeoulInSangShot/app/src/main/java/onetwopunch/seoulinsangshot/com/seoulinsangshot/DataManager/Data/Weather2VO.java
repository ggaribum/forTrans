package onetwopunch.seoulinsangshot.com.seoulinsangshot.DataManager.Data;

import java.util.ArrayList;

import onetwopunch.seoulinsangshot.com.seoulinsangshot.Model.Model_Weather2;

/**
 * Created by Beom2 on 2017-09-24.
 */

public class Weather2VO {
    Response response;

    public class Response {

        Header header;
        Body body;
        ///////////////
        public class Header {
            String resultCode;
            String resultMsg;

            public String getResultCode() {
                return resultCode;
            }

            public String getResultMsg() {
                return resultMsg;
            }
        }
        ///////////////////////
        public class Body {
            Items items;

            public class Items {
                ArrayList<Model_Weather2> item= new ArrayList<Model_Weather2>();

                //getItem()
                public ArrayList<Model_Weather2> getItem() {
                    return item;
                }

            }
            //getItems()
            public Items getItems() {
                return items;
            }
        }
        ///////헤더는 필요없다///////
        public Header getHeader() {
            return header;
        }
        //////////////////

        public Body getBody() {
            return body;
        }

    }

    public Response getResponse() {
        return response;
    }
}