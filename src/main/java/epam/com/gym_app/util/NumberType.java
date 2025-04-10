package epam.com.gym_app.util;

import org.hibernate.engine.spi.SharedSessionContractImplementor;
import org.hibernate.usertype.UserType;

import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;

public class NumberType implements UserType<Number> {

    @Override
    public int getSqlType() {
        return Types.NUMERIC;
    }

    @Override
    public Class<Number> returnedClass() {
        return Number.class;
    }

    @Override
    public boolean equals(Number x, Number y) {
        if (x == null || y == null) {
            return false;
        }
        return x.equals(y);
    }

    @Override
    public int hashCode(Number x) {
        return x != null ? x.hashCode() : 0;
    }

    @Override
    public Number nullSafeGet(ResultSet rs, int position, SharedSessionContractImplementor session, Object owner) throws SQLException {
        BigDecimal value = rs.getBigDecimal(position);
        return value != null ? value : null;
    }

    @Override
    public void nullSafeSet(PreparedStatement st, Number value, int index, SharedSessionContractImplementor session) throws SQLException {
        if (value == null) {
            st.setNull(index, Types.NUMERIC);
        } else {
            st.setBigDecimal(index, new BigDecimal(value.toString()));
        }
    }

    @Override
    public Number deepCopy(Number value) {
        return value != null ? new BigDecimal(value.toString()) : null;
    }

    @Override
    public boolean isMutable() {
        return true;
    }

    @Override
    public Serializable disassemble(Number value) {
        return value != null ? value.toString() : null;
    }

    @Override
    public Number assemble(Serializable cached, Object owner) {
        return cached != null ? new BigDecimal((String) cached) : null;
    }
}
