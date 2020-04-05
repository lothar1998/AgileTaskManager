package persistence.repositiories;

import persistence.AbstractRepository;
import persistence.ReadRepository;
import persistence.entities.ProgressEntity;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class ProgressRepository extends AbstractRepository implements ReadRepository<ProgressEntity> {
    @Override
    public ProgressEntity get(Integer arg) {
        String sql = "SELECT * FROM agile.progresses WHERE id = ?";
        return dataAccessLayer.executeQuery(connection -> {
            PreparedStatement statement = connection.prepareStatement(sql);

            statement.setInt(1, arg);
            ResultSet result = statement.executeQuery();


            ProgressEntity progressEntity = new ProgressEntity();

            if(result.next()){
                progressEntity.setId(result.getInt("id"));
                progressEntity.setName(result.getString("name"));
                progressEntity.setDescription(result.getString("description"));
            }

            statement.close();
            return progressEntity;
        });
    }

    @Override
    public List<ProgressEntity> getAll() {
        String sql = "SELECT * FROM agile.progresses";
        return dataAccessLayer.executeQuery(connection -> {
            PreparedStatement statement = connection.prepareStatement(sql);

            ResultSet result = statement.executeQuery();


            List<ProgressEntity> progresses = new ArrayList<>();

            while (result.next()){
                ProgressEntity progressEntity = new ProgressEntity();
                progressEntity.setId(result.getInt("id"));
                progressEntity.setName(result.getString("name"));
                progressEntity.setDescription(result.getString("description"));

                progresses.add(progressEntity);
            }

            statement.close();
            return progresses;
        });
    }
}
