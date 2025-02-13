
// package hello.fclover.facade;

// import hello.fclover.service.StockService;
// import lombok.NoArgsConstructor;
// import org.redisson.api.RLock;
// import org.redisson.api.RedissonClient;
// import org.springframework.stereotype.Component;

// import java.util.concurrent.TimeUnit;

// @Component
// public class RedissonLockStockFacade {

//     private RedissonClient redissonClient;
//     private StockService stockService;


//     public RedissonLockStockFacade(RedissonClient redissonClient, StockService stockService) {
//         this.redissonClient = redissonClient;
//         this.stockService = stockService;
//     }

//     public void decrease(int id, int quantity){
//         RLock lock = redissonClient.getLock(Integer.toString(id));


//         try{
//             boolean avilable = lock.tryLock(30,5, TimeUnit.SECONDS);

//             if(!avilable){
//                 System.out.println("Lock 획득실패");
//                 return;
//             }

//             stockService.decrease(id, quantity);

//         } catch (InterruptedException e) {
//             throw new RuntimeException(e);
//         }finally {
//             lock.unlock();
//         }

//     }
// }

