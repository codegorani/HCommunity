package org.hcom.models.common;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode
public class ResponseResult<T> {
    private String type;
    private T data;

    public ResponseResult(T data) {
        this.data = data;
    }

    public static <T> ResponseResult<T> responseResult(T data) {
        if(data == null) {
            return null;
        }
        ResponseResult<T> result = new ResponseResult<>(data);
        result.setType(data.getClass().getTypeName());
        return result;
    }

    public void setType(String type) {
        this.type = type;
    }
}
