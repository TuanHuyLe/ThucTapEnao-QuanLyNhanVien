package com.enao.team2.quanlynhanvien.service;

public interface IMessageService<T> {
    boolean sendMessage(T message);
}
