package com.example.coderamabackend.rate;

import com.example.coderamabackend.budgetItem.EntityBudgetItem;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class JoinedRate {
    private EntityRate rate;
    private EntityBudgetItem budgetItem;
}
