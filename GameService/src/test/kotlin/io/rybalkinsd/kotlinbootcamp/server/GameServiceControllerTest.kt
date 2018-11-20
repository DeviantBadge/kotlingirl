package io.rybalkinsd.kotlinbootcamp.server

import org.junit.Test
import org.junit.runner.RunWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.MediaType
import org.springframework.test.context.junit4.SpringRunner
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post
import org.springframework.test.web.servlet.result.MockMvcResultMatchers
import org.springframework.test.web.servlet.setup.MockMvcBuilders
import org.springframework.web.context.WebApplicationContext

@RunWith(SpringRunner::class)
@SpringBootTest
class GameServiceControllerTest {
/*
    @Autowired
    lateinit var context: WebApplicationContext

    @Test
    fun `history test`() {
        val mockMvc: MockMvc = MockMvcBuilders
                .webAppContextSetup(context)
                .build()

        val p = mockMvc.perform(get("/chat/history"))
                .andExpect(MockMvcResultMatchers.status().isOk)
                .andReturn()
        println(p.response.contentAsString)
    }

    @Test
    fun `simple login and logout test`() {
        val mockMvc: MockMvc = MockMvcBuilders
                .webAppContextSetup(context)
                .build()
        mockMvc.perform(post("/chat/login").contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .param("name", "abbar").param("password", "111"))
                .andExpect { it.response.status == 200 || it.response.contentAsString == "Already logged in" }
                .andDo { result ->
                    println(result.response.contentAsString)
                }
        mockMvc.perform(post("/chat/logout").contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .param("name", "abbar").param("password", "111"))
                .andExpect(MockMvcResultMatchers.status().isOk)
                .andDo { result ->
                    println(result.response.contentAsString)
                }
    }

    @Test
    fun `login test with empty password`() {
        val mockMvc: MockMvc = MockMvcBuilders
                .webAppContextSetup(context)
                .build()
        mockMvc.perform(post("/chat/login").contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .param("name", "abbas").param("password", ""))
                .andExpect(MockMvcResultMatchers.status().isBadRequest)
                .andDo { result ->
                    println(result.response.contentAsString)
                }
    }

    @Test
    fun `logout test`() {
        var mockMvc: MockMvc = MockMvcBuilders
                .webAppContextSetup(context)
                .build()

        mockMvc.perform(post("/chat/login").contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .param("name", "abba").param("password", "111"))
                .andExpect { it.response.status == 200 || it.response.contentAsString == "Already logged in" }
                .andDo { result ->
                    println(result.response.contentAsString)
                }

        mockMvc.perform(post("/chat/logout").contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .param("name", "abba").param("password", "112"))
                .andExpect(MockMvcResultMatchers.status().isBadRequest)
                .andDo { result ->
                    println(result.response.contentAsString)
                }

        mockMvc.perform(post("/chat/logout").contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .param("name", "abba").param("password", "111"))
                .andExpect(MockMvcResultMatchers.status().isOk)
                .andDo { result ->
                    println(result.response.contentAsString)
                }
    }

    @Test
    fun `logout  with empty name`() {
        val mockMvc: MockMvc = MockMvcBuilders
                .webAppContextSetup(context)
                .build()
        mockMvc.perform(post("/chat/logout").contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .param("name", "").param("password", "1111"))
                .andExpect(MockMvcResultMatchers.status().isBadRequest)
                .andDo { result ->
                    println(result.response.contentAsString)
                }
    }

    @Test
    fun `logout  with inexistent account`() {
        val mockMvc: MockMvc = MockMvcBuilders
                .webAppContextSetup(context)
                .build()
        mockMvc.perform(post("/chat/logout").contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .param("name", "1").param("password", "1111"))
                .andExpect(MockMvcResultMatchers.status().isBadRequest)
                .andDo { result ->
                    println(result.response.contentAsString)
                }
    }

    @Test
    fun `login 2 times test`() {
        var mockMvc = MockMvcBuilders
                .webAppContextSetup(context)
                .build()
        mockMvc.perform(post("/chat/login").contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .param("name", "abbad").param("password", "1"))
                .andExpect { it.response.status == 200 || it.response.contentAsString == "Already logged in" }
                .andDo { result ->
                    println(result.response.contentAsString)
                }

        mockMvc.perform(post("/chat/login").contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .param("name", "abbad").param("password", "1"))
                .andExpect(MockMvcResultMatchers.status().isBadRequest)
                .andDo { result ->
                    println(result.response.contentAsString)
                }

        mockMvc.perform(post("/chat/logout").contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .param("name", "abbad").param("password", "1"))
                .andExpect(MockMvcResultMatchers.status().isOk)
                .andDo { result ->
                    println(result.response.contentAsString)
                }
    }

    @Test
    fun `login test with large name`() {
        val mockMvc: MockMvc = MockMvcBuilders
                .webAppContextSetup(context)
                .build()
        mockMvc.perform(post("/chat/login").contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .param("name", "abbaasjdafjskdgjbskjbfhsgbkrhbdrhgbdj").param("password", "1"))
                .andExpect(MockMvcResultMatchers.status().isBadRequest)
                .andDo { result ->
                    println(result.response.contentAsString)
                }
    }

    @Test
    fun `say test`() {

        var mockMvc = MockMvcBuilders
                .webAppContextSetup(context)
                .build()
        /*mockMvc.perform(post("/chat/logout").contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .param("name", "VMiK").param("password", "123"))
                .andExpect(MockMvcResultMatchers.status().isOk)
                .andDo{
                    result -> println(result.response.contentAsString)
                }*/

        mockMvc.perform(post("/chat/login").contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .param("name", "VMiK")
                .param("password", "123"))
                .andExpect { it.response.status == 200 || it.response.contentAsString == "Already logged in" }
                .andDo { result ->
                    println(result.response.contentAsString)
                }

        mockMvc.perform(post("/chat/say").contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .param("name", "VMiK")
                .param("password", "123")
                .param("msg", "111111"))
                .andDo { println(it.response.contentAsString) }
                .andExpect(MockMvcResultMatchers.status().isOk)
                .andReturn()

        mockMvc.perform(post("/chat/logout").contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .param("name", "VMiK").param("password", "123"))
                .andExpect(MockMvcResultMatchers.status().isOk)
                .andDo { result ->
                    println(result.response.contentAsString)
                }
    }

    @Test
    fun online() {
        val mockMvc = MockMvcBuilders
                .webAppContextSetup(context)
                .build()
        val p = mockMvc.perform(get("/chat/online").contentType(MediaType.APPLICATION_FORM_URLENCODED))
                .andExpect(MockMvcResultMatchers.status().isOk)
                .andReturn()
        println(p.response.contentAsString)
    }
*/
}
