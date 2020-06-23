package xyz.majexh.workflow.domain;

import lombok.Data;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;

/**
 * @author majexh
 */
@Data
@Document(collection = "metaData")
public class RtkMetaData {

    private String name;
    private Date date;
}
