package org.aimrobot.server4j.framework;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * @program: neomay
 * @description:
 * @author: H4rry217
 **/

@Getter
@Setter
public class RequestResult implements Serializable {

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Map<String, Object> data;

    private int statusCode = 0;

    private String msg;

    private boolean ok = false;

    public RequestResult putData(String dataKey, Object data){
        if(this.data == null){
            this.data = new LinkedHashMap<>();
        }

        this.data.put(dataKey, data);

        return this;
    }

    public RequestResult putData(Object data){
        return this.putData("result", data);
    }

    public RequestResult withData(Object data){
        return this.putData("result", data);
    }

    public RequestResult withOriginMsg(String msg){
        this.setMsg(msg);
        return this;
    }

}
