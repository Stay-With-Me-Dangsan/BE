package stay.with.me.spring.mybatis;

import org.apache.ibatis.type.BaseTypeHandler;
import org.apache.ibatis.type.JdbcType;
import org.postgresql.geometric.PGpoint;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class PGPointTypeHandler extends BaseTypeHandler<PGpoint> {

    @Override
    public void setNonNullParameter(PreparedStatement ps, int i, PGpoint parameter, JdbcType jdbcType) throws SQLException {
        ps.setObject(i, parameter);
    }

    @Override
    public PGpoint getNullableResult(ResultSet rs, String columnName) throws SQLException {
        return (PGpoint) rs.getObject(columnName);
    }

    @Override
    public PGpoint getNullableResult(ResultSet rs, int columnIndex) throws SQLException {
        return (PGpoint) rs.getObject(columnIndex);
    }

    @Override
    public PGpoint getNullableResult(java.sql.CallableStatement cs, int columnIndex) throws SQLException {
        return (PGpoint) cs.getObject(columnIndex);
    }

}
