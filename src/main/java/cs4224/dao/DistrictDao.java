package cs4224.dao;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.dbutils.QueryRunner;

public class DistrictDao {
    private final ObjectMapper objectMapper;
    private final QueryRunner queryRunner;
    private final String schema;

    public DistrictDao(final ObjectMapper objectMapper, final QueryRunner queryRunner, final String schema) {
        this.objectMapper = objectMapper;
        this.queryRunner = queryRunner;
        this.schema = schema;
    }
}
