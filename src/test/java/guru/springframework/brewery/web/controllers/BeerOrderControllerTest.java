package guru.springframework.brewery.web.controllers;

import guru.springframework.brewery.services.BeerOrderService;
import guru.springframework.brewery.web.model.BeerDto;
import guru.springframework.brewery.web.model.BeerOrderDto;
import guru.springframework.brewery.web.model.BeerOrderLineDto;
import guru.springframework.brewery.web.model.BeerOrderPagedList;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.UUID;

import static org.mockito.Mockito.reset;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@WebMvcTest(BeerOrderController.class)
class BeerOrderControllerTest {

    @MockBean
    BeerOrderService beerOrderService;

    @Autowired
    MockMvc mockMvc;

    BeerDto validBeer;
    BeerOrderDto beerOrder;
    BeerOrderPagedList beerOrderPagedList;

    @BeforeEach
    void setUp() {
//        System.out.println(UUID.randomUUID());
        validBeer = BeerDto.builder()
                .id(UUID.randomUUID())
                .version(1)
                .beerName("Beer1")
                .price(new BigDecimal("12.99"))
                .quantityOnHand(4)
                .upc(123123213124L)
                .createdDate(OffsetDateTime.now())
                .lastModifiedDate(OffsetDateTime.now())
                .build();
        beerOrder=BeerOrderDto.builder()
                .id(UUID.randomUUID())
                .customerRef("1234")
                .beerOrderLines(List.of(BeerOrderLineDto
                        .builder()
                        .beerId(validBeer.getId())
                        .build()))
                .build();
        beerOrderPagedList=new BeerOrderPagedList(List.of(beerOrder),
                PageRequest.of(1,1),1L);
    }

    @AfterEach
    void tearDown() {
        reset(beerOrderService);
    }

    @Test
    void listOrders() throws Exception {
        given(beerOrderService.listOrders(any(),any())).willReturn(beerOrderPagedList);

        mockMvc.perform(get("/api/v1/customers/7a2369aa-30b1-4a26-b449-17396001a4f8/orders"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8));
    }

    @Test
    void getOrder() throws Exception {
        given(beerOrderService.getOrderById(any(),any())).willReturn(beerOrder);

        mockMvc.perform(get("/api/v1/customers/7a2369aa-30b1-4a26-b449-17396001a4f8/orders/b609d01f-81ca-4186-bf5d-9691709844b0"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8));
    }
}