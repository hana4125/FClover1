package hello.fclover.service;

import hello.fclover.mybatis.mapper.BackOfficeMapper;
import hello.fclover.mybatis.mapper.StockMapper;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;

import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@SpringBootTest
public class StockServiceImplTest {

    @Mock
    private StockMapper dao;  // BackOfficeMapper를 Mocking

    @InjectMocks
    private StockServiceImpl stockService;  //

    @BeforeEach
    public void setUp() {
        // 이 곳에 테스트 실행 전에 필요한 초기화 코드가 들어갑니다.
    }

    @Test
    public void testDecrease() {
        // Given
        int id = 1;  // 테스트용 id 값
        int quantity = 5;  // 테스트용 수량 값

        // When
        stockService.decrease(id, quantity);  // decrease 메서드 호출

        // Then
        // dao의 decrease 메서드가 1번 호출되었는지 검증
        verify(dao, times(1)).decrease(quantity);
    }

}
