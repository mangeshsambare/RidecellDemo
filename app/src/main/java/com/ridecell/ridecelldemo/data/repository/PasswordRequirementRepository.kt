package com.ridecell.ridecelldemo.data.repository

import com.ridecell.ridecelldemo.data.PasswordRequirementDataSource
import com.ridecell.ridecelldemo.data.model.PasswordRequirementsDto

class PasswordRequirementRepository(val dataSource: PasswordRequirementDataSource){

    fun getPasswordRequirements(): PasswordRequirementsDto?{
        if (dataSource.passwordRequirement == null) {
            dataSource.getPasswordRequirements()
        }
        return dataSource.passwordRequirement
    }
}
