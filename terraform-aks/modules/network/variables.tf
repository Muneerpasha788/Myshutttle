variable "rg_name" { type = string }
variable "location" { type = string }
variable "vnet_name" { type = string }
variable "subnet_name" { type = string }
variable "address_space" { type = list(string) }
variable "subnet_prefix" { type = list(string) }
