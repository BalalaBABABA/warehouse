package com.abc.warehouse.dto;

import lombok.Data;

import java.util.List;

@Data
public class EncryotResult extends Result{
        private Boolean success;
//        private int code;
        private String errorMsg="null";
        private Object data="null";
        private Long total = 1L;
        private String aesKey="null";
        private String publicKey="null";

        public EncryotResult() {
        }

        public EncryotResult(Boolean success, Object data) {
            this.success = success;
            this.data = data;
        }
        public EncryotResult(Boolean success, String errorMsg, Object data, Long total) {
            this.success = success;
            this.errorMsg = errorMsg;
            this.data = data;
            this.total = total;
        }
        public static EncryotResult ok(Object data){
                return new EncryotResult(true, data);
        }

        public static EncryotResult ok(List<?> data, Long total){
            return new EncryotResult(true, null, data, total);
        }

        public static EncryotResult fail(String errorMsg){
                EncryotResult encryotResult= new EncryotResult();
                encryotResult.setSuccess(false);
                encryotResult.setErrorMsg(errorMsg);
                return encryotResult;
        }



}
