package com.kafka.carbongraphvisual.bean.VO;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Data
public class TransactionModelVO extends TransactionVO{

    private static final long serialVersionUID = 1L;

    /**
     * 模型
     */
    private List<VertexVO> vertices;
    private List<EdgeVO> edges;
}
