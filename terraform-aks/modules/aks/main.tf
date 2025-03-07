resource "azurerm_kubernetes_cluster" "aks" {
  name                = var.aks_name
  location            = var.location
  resource_group_name = var.rg_name
  dns_prefix          = var.dns_prefix

  default_node_pool {
    name       = "nodepool"
    node_count = var.node_count
    vm_size    = var.node_vm_size
    vnet_subnet_id = var.subnet_id
  }

  identity {
    type = "SystemAssigned"
  }
network_profile {
    network_plugin = "azure"
    service_cidr   = "10.1.0.0/16"  # Change to avoid conflict
    dns_service_ip = "10.1.0.10"    # Ensure it falls within the service_cidr
  }
}
