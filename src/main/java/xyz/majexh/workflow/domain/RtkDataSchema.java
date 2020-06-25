package xyz.majexh.workflow.domain;

import lombok.Data;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;

/**
 * @author majexh
 */
@Data
@Document
public class RtkDataSchema {

    private Date time;
    private Double eBase;
    private Double nBase;
    private Double uBase;
}
