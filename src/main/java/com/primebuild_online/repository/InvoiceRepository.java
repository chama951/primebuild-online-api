package com.primebuild_online.repository;

import com.primebuild_online.model.Cart;
import com.primebuild_online.model.Invoice;
import com.primebuild_online.model.Item;
import com.primebuild_online.model.User;
import com.primebuild_online.model.enumerations.InvoiceStatus;
import com.primebuild_online.model.enumerations.Privileges;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;

public interface InvoiceRepository  extends JpaRepository<Invoice, Long> {
    List<Invoice> findAllByUser(User user);

    List<Invoice> findAllByUser_UserId(Long userId);

    List<Invoice> findAllByInvoiceDate(LocalDate invoiceDate);

    List<Invoice> findAllByInvoiceStatus(InvoiceStatus invoiceStatus);

    List<Invoice> findDistinctByInvoiceItems_Item(Item item);

    List<Invoice> findByUserRoleRolePrivilegeListPrivilege(Privileges privileges);

    List<Invoice> findDistinctByInvoiceItems_ItemAndInvoiceStatus(Item invoiceItemsItem, InvoiceStatus invoiceStatus);
}
