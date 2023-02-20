package com.lonni.im.core.serializable;

import lombok.Getter;

import java.util.Arrays;

/**
 * <h1>序列化算法枚举</h1>
 * <ul>
 *     <li>0:javajdk序列化</li>
 *     <li>1:json(Gson)序列化</li>
 *     <li>2:Hessian序列化</li>
 *     <li>3:ProtoStuff序列化</li>
 *     <li>4:kryo序列化 <h2>注意:该算法未实现</h2></li>
 *
 * </ul>
 *
 * @author: Lonni
 * @date: 2023/2/7 0007 10:19
 */
@Getter
public enum SerializeAlgorithmEnum {
    /**
     * JDK
     */
    JDK((byte) 0),
    /**
     * Json (Gson)
     */
    GSON_JSON((byte) 1),
    /**
     * hessian
     */
    HESSIAN((byte) 2),

    /**
     * protoStuff
     */
    PROTO_STUFF((byte) 3),

    /**
     * kryo 未实现
     */
    @Deprecated
    KRYO((byte) 4);


    private byte type;

    SerializeAlgorithmEnum(byte type) {
        this.type = type;
    }

    public byte getType() {
        return type;
    }

    public int getTypeToInt() {
        return type;
    }

    /**
     * get the enum class
     *
     * @param type the type
     * @return the enum class
     */
    public static SerializeAlgorithmEnum getEnum(Byte type) {
        return type == null ? null : Arrays.stream(values())
                .filter(t -> t.getType() == type)
                .findFirst()
                .orElse(null);
    }

}
