package xyz.majexh.workflow.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import xyz.majexh.workflow.domain.RtkDataSchema;
import xyz.majexh.workflow.service.RtkDataService;

import javax.annotation.Resource;
import java.lang.reflect.Method;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author majexh
 */
@Controller
@RequestMapping(path = "/deformationGraph")
public class DeformationGraphController {

    private static final String MONGO_AFTER_PROCESS_POSTFIX = "postprocess";

    @Resource
    private RtkDataService rtkDataService;

    @GetMapping("/allRtkDataRepoName")
    public ResponseEntity<HashMap<String, Object>> getAllRtkDataRepoName() {
        Set<String> allNames = rtkDataService.getAllCollectionsName();
        List<String> res = allNames
                .stream()
                .filter((name) -> name.contains(MONGO_AFTER_PROCESS_POSTFIX))
                .collect(Collectors.toList());
        return ResEntity.okDefault(res);
    }

    @GetMapping("/rtkDataByTimeRange")
    public ResponseEntity<HashMap<String, Object>> getRtkDataByTimeRange(@RequestParam Long startTime,
                                                                         @RequestParam Long endTime,
                                                                         @RequestParam String collectionName,
                                                                         @RequestParam String type) throws Exception {
        List<RtkDataSchema> res = rtkDataService.getRtkDataSchemaByTimeArrange(new Date(startTime), new Date(endTime), collectionName, type);

        List<Long> categories = new ArrayList<>();
        List<Double> values = new ArrayList<>();

        Class<RtkDataSchema> rtkDataSchemaClazz = RtkDataSchema.class;
        Method method = rtkDataSchemaClazz.getMethod("get" + firstStrUpper(type));

        for (RtkDataSchema entity : res) {
            categories.add(entity.getTime().getTime());
            values.add((Double) method.invoke(entity));
        }

        return ResEntity.okDefault(new HashMap<>(){{
            put("categoryData", categories);
            put("valueData", values);
        }});
    }

    private String firstStrUpper(String type) {
        char[] cs = type.toCharArray();
        cs[0] -= 32;
        return String.valueOf(cs);
    }

    @GetMapping("/rtkTimeRangeByCollectionName")
    public ResponseEntity<HashMap<String, Object>> getRtkTimeRangeByCollectionName(@RequestParam String collectionName) {
        return ResEntity.okDefault(rtkDataService.getTimeRangeByCollectionName(collectionName));
    }
}
