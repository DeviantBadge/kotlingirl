package ru.atom.game;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class MatchmakerTest {

    @Autowired
    private MockMvc mock;
    @Before
    public void setUp() throws Exception {
    }

    @Test
    public void joinTest() throws Exception {
        mock.perform(post("/join").param("name","casya")).andDo(print());
    }

    @Test
    public void testTest() throws Exception{
        mock.perform(get("/test")).andDo(print());
    }
}