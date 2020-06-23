package xyz.majexh.workflow.domain;

import lombok.Data;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * @author majexh
 */
@Data
@Document
public class RtkDataSchema {

    private Double time;
    private Double eBase;
    private Double nBase;
    private Double uBase;
}
