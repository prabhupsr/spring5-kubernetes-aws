package com.sample.kuber;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;

public interface MerchantRepo extends ReactiveMongoRepository<Merchant,Integer> {
}
