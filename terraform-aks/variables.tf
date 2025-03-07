variable "rg_name" { type = string }
variable "location" { type = string }
variable "vnet_name" { type = string }
variable "subnet_name" { type = string }
variable "address_space" { type = list(string) }
variable "subnet_prefix" { type = list(string) }
variable "aks_name" { type = string }
variable "dns_prefix" { type = string }
variable "node_count" { type = number }
variable "node_vm_size" { type = string }
variable "key_vault_name" {}
variable "tenant_id" {
  default = ""
}
variable "subscription_id" {
  default = ""
}
variable "client_id" {
  default = ""
}
variable "client_secret" {
  default = ""
}
