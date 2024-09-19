package com.tcarroll10.finance.service;

import org.junit.jupiter.api.BeforeEach;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import com.tcarroll10.findata.repo.FindataApiRepo;
import com.tcarroll10.findata.service.FindataApiServiceImpl;

public class FindataApiServiceImplTest {

  @Mock
  private FindataApiRepo repo;

  @InjectMocks
  private FindataApiServiceImpl service;

  @BeforeEach
  public void setUp() {
    MockitoAnnotations.openMocks(this); // Initialize mocks
  }



}
