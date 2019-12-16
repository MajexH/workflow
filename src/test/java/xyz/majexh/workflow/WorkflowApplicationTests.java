package xyz.majexh.workflow;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Bean;
import xyz.majexh.workflow.Utils.ConfigGetter;
import xyz.majexh.workflow.Utils.WorkflowConfigGetter;
import xyz.majexh.workflow.workflow.builder.TopologyBuilder;
import xyz.majexh.workflow.workflow.entity.def.Node;

import java.util.HashMap;

@SpringBootTest
class WorkflowApplicationTests {

    @Test
    void contextLoads() {
    }

}
