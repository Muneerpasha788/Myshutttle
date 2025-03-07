rg_name         = "myResourceGroup"
location        = "eastus"
vnet_name       = "myVNet"
subnet_name     = "mySubnet"
address_space   = ["10.2.0.0/16"]   # Change VNet range
subnet_prefix   = ["10.2.1.0/24"]   # Change Subnet range
aks_name        = "myAKSCluster"
dns_prefix      = "myaksdns"
node_count      = 2
node_vm_size    = "Standard_D2s_v3"
key_vault_name  = "key-vault-terraform-az"
tenant_id       = "a66d5ec3-5a49-4717-8a32-fab09735caab"
subscription_id = "6996530b-3210-4e6d-81ef-9a47f5bec1c3"
