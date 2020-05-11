import com3001.at00672.model.MessageRepository;
import com3001.at00672.*;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
@SpringBootTest(classes=WebApplication.class)
@AutoConfigureMockMvc
public class WebApplicationTest {

    private MessageRepository messageRepository;

    @Test
    void applicationLoads() {

    }
}
