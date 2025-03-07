data "azurerm_key_vault" "vault" {
  name                = var.key_vault_name
  resource_group_name = "key-vault"
}
data "azurerm_key_vault_secret" "tenant_id" {
  name         = "tenant-id"
  key_vault_id = data.azurerm_key_vault.vault.id
}

data "azurerm_key_vault_secret" "subscription_id" {
  name         = "subscription-id"
  key_vault_id = data.azurerm_key_vault.vault.id
}

data "azurerm_key_vault_secret" "client_id" {
  name         = "client-id"
  key_vault_id = data.azurerm_key_vault.vault.id
}

data "azurerm_key_vault_secret" "client_secret" {
  name         = "client-secret"
  key_vault_id = data.azurerm_key_vault.vault.id
}

# 3️⃣ Provider Block (Uses Variables)
provider "azurerm" {
  features {}

  tenant_id       = var.tenant_id
  subscription_id = var.subscription_id
  client_id       = var.client_id
  client_secret   = var.client_secret
}
terraform {
  required_version = "= 1.6.6"

  backend "azurerm" {
    resource_group_name  = "terraform-backend-rg"
    storage_account_name = "terraformbackendsa11"
    container_name       = "akstfstate"
    key                  = "terraform.tfstate"

  }
}

module "resource_group" {
  source   = "./modules/resource_group"
  rg_name  = var.rg_name
  location = var.location
}

module "network" {
  source        = "./modules/network"
  rg_name       = module.resource_group.rg_name
  location      = var.location
  vnet_name     = var.vnet_name
  subnet_name   = var.subnet_name
  address_space = var.address_space
  subnet_prefix = var.subnet_prefix
}

module "aks" {
  source       = "./modules/aks"
  rg_name      = module.resource_group.rg_name
  location     = var.location
  aks_name     = var.aks_name
  dns_prefix   = var.dns_prefix
  subnet_id    = module.network.subnet_id
  node_count   = var.node_count
  node_vm_size = var.node_vm_size
}
