package com.example.usermanagement.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class DummyJsonResponseDto {
    private List<DummyJsonUserDto> users;
    private Integer total;
    private Integer skip;
    private Integer limit;

    public DummyJsonResponseDto() {}

    public List<DummyJsonUserDto> getUsers() { return users; }
    public void setUsers(List<DummyJsonUserDto> users) { this.users = users; }

    public Integer getTotal() { return total; }
    public void setTotal(Integer total) { this.total = total; }

    public Integer getSkip() { return skip; }
    public void setSkip(Integer skip) { this.skip = skip; }

    public Integer getLimit() { return limit; }
    public void setLimit(Integer limit) { this.limit = limit; }
}