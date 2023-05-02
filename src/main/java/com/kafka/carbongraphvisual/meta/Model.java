package com.kafka.carbongraphvisual.meta;

import com.kafka.carbongraphvisual.bean.VO.EdgeVO;
import com.kafka.carbongraphvisual.bean.VO.VertexVO;
import lombok.Data;

import java.util.List;

@Data
public class Model {

    List<VertexVO> vertices;
    List<EdgeVO> edges;
}
