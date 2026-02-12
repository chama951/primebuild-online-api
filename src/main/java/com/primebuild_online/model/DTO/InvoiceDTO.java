package com.primebuild_online.model.DTO;

import com.primebuild_online.model.Item;

import java.util.ArrayList;
import java.util.List;

public class InvoiceDTO {
    private String invoiceStatus;
    private List<Item> itemList = new ArrayList<>();
}
