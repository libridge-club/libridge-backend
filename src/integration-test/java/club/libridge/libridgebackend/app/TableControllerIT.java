package club.libridge.libridgebackend.app;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import club.libridge.libridgebackend.app.controller.TableController;
import club.libridge.libridgebackend.app.persistence.TableEntity;

@SpringBootTest()
@ActiveProfiles("development")
public class TableControllerIT {

    @Autowired
    private TableController controller;

    @Test
    public void getTables_ShouldReturnAllExistingTables() {
        List<TableEntity> tables = controller.getTables();

        assertEquals(0, tables.size());

        controller.createTable();
        List<TableEntity> tables2 = controller.getTables();
        assertEquals(1, tables2.size());
    }

}
