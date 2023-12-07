package com.bsj.dayonetest.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RedisService {

  private final RedisTemplate<String, String> redisTemplate;

  public String get(String key) {
    return redisTemplate.opsForValue().get(key);
  }

  public void set(String key, String value) {
    redisTemplate.opsForValue().set(key, value);
  }
}
