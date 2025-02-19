package ca.jrvs.apps.stockquote.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class PositionDao implements CrudDao<Position, String> {

    private static final String INSERT = "INSERT INTO position (symbol, number_of_shares, value_paid) VALUES (?, ?, ?);";
    private static final String UPDATE = "UPDATE position SET number_of_shares = ?, value_paid = ? WHERE symbol = ?;";
    private static final String SELECT_BY_ID = "SELECT symbol, number_of_shares, value_paid FROM position" +
            " WHERE symbol = ?;";
    private static final String SELECT_ALL = "SELECT symbol, number_of_shares, value_paid FROM position;";
    private static final String DELETE = "DELETE FROM position WHERE symbol = ?;";
    private static final String DELETE_ALL = "TRUNCATE TABLE position;";

    private Connection c;

    public PositionDao(Connection c) {
        this.c = c;
    }

    @Override
    public Position save(Position entity) throws IllegalArgumentException {
        Position position;
        Optional<Position> optionalPosition = findById(entity.getTicker());
        if(optionalPosition.isPresent()) {
            position = update(entity);
        } else {
            position = insert(entity);
        }
        return position;
    }

    private Position insert(Position entity) throws IllegalArgumentException {
        try (PreparedStatement statement = c.prepareStatement(INSERT)) {
            statement.setString(1, entity.getTicker());
            statement.setInt(2, entity.getNumOfShares());
            statement.setDouble(3, entity.getValuePaid());
            statement.execute();
            return findById(entity.getTicker()).orElse(null);
        } catch (SQLException e) {
            e.printStackTrace();
            throw new IllegalArgumentException(e);
        }
    }

    private Position update(Position entity) throws IllegalArgumentException {
        try (PreparedStatement statement = c.prepareStatement(UPDATE)) {
            statement.setInt(1, entity.getNumOfShares());
            statement.setDouble(2, entity.getValuePaid());
            statement.setString(3, entity.getTicker());
            statement.execute();
            return findById(entity.getTicker()).orElse(null);
        } catch (SQLException e) {
            e.printStackTrace();
            throw new IllegalArgumentException(e);
        }
    }

    @Override
    public Optional<Position> findById(String s) throws IllegalArgumentException {
        Position position = null;
        try(PreparedStatement statement = c.prepareStatement(SELECT_BY_ID)) {
            statement.setString(1, s);
            ResultSet rs = statement.executeQuery();
            while(rs.next()) {
                position = new Position();
                position.setTicker(rs.getString("symbol"));
                position.setNumOfShares(rs.getInt("number_of_shares"));
                position.setValuePaid(rs.getDouble("value_paid"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new IllegalArgumentException(e);
        }
        return Optional.ofNullable(position);
    }

    @Override
    public Iterable<Position> findAll() {
        List<Position> results = new ArrayList<>();
        try(PreparedStatement statement = c.prepareStatement(SELECT_ALL)) {
            ResultSet rs = statement.executeQuery();
            while(rs.next()) {
                Position position = new Position();
                position.setTicker(rs.getString("symbol"));
                position.setNumOfShares(rs.getInt("number_of_shares"));
                position.setValuePaid(rs.getDouble("value_paid"));
                results.add(position);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new IllegalArgumentException(e);
        }
        return results;
    }

    @Override
    public void deleteById(String s) throws IllegalArgumentException {
        try(PreparedStatement statement = c.prepareStatement(DELETE)) {
            statement.setString(1, s);
            statement.execute();
        } catch (SQLException e) {
            e.printStackTrace();
            throw new IllegalArgumentException(e);
        }
    }

    @Override
    public void deleteAll() throws IllegalArgumentException {
        try(PreparedStatement statement = c.prepareStatement(DELETE_ALL)) {
            statement.execute();
        } catch (SQLException e) {
            e.printStackTrace();
            throw new IllegalArgumentException(e);
        }
    }
}
