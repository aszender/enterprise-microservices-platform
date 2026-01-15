// features/inventory/pages/InventoryPage.jsx
// Inventory page: view stock levels

import { RefreshCw, Warehouse, AlertTriangle, CheckCircle, AlertCircle } from 'lucide-react'
import { useInventory } from '../hooks/useInventory'
import { Button, Card, CardContent, CardHeader, CardTitle } from '../../../shared/ui'

export default function InventoryPage() {
  const { items: stockItems, errorMessage, busy, count, refresh } = useInventory()

  const getStockStatus = (available) => {
    if (available <= 5) {
      return {
        label: 'Low Stock',
        icon: AlertTriangle,
        className: 'bg-destructive/20 text-destructive border-destructive/30',
        textClass: 'text-destructive',
      }
    }
    if (available <= 20) {
      return {
        label: 'Medium',
        icon: AlertCircle,
        className: 'bg-yellow-500/20 text-yellow-300 border-yellow-500/30',
        textClass: 'text-yellow-300',
      }
    }
    return {
      label: 'In Stock',
      icon: CheckCircle,
      className: 'bg-green-500/20 text-green-300 border-green-500/30',
      textClass: 'text-green-300',
    }
  }

  return (
    <main className="mx-auto w-full max-w-5xl px-6 py-10">
      <header className="mb-8">
        <div className="flex items-center gap-3">
          <Warehouse className="h-8 w-8 text-muted-foreground" />
          <h1 className="text-3xl font-bold tracking-tight">Inventory</h1>
          <span className="rounded-full bg-secondary px-2.5 py-0.5 text-xs font-medium text-muted-foreground">
            {count} items
          </span>
        </div>
        <p className="mt-2 text-muted-foreground">View current stock levels and reservations across products</p>
      </header>

      {/* Error message */}
      {errorMessage && (
        <p className="mb-4 rounded-lg border border-destructive/30 bg-destructive/10 p-3 text-sm text-destructive">
          {errorMessage}
        </p>
      )}

      {/* STOCK LIST */}
      <Card>
        <CardHeader className="flex flex-row items-center justify-between">
          <CardTitle>Stock Levels</CardTitle>
          <Button variant="outline" size="sm" onClick={refresh} disabled={busy}>
            <RefreshCw className={`mr-1 h-3 w-3 ${busy ? 'animate-spin' : ''}`} />
            Refresh
          </Button>
        </CardHeader>
        <CardContent>
          {stockItems.length === 0 ? (
            <p className="text-sm text-muted-foreground">No stock items yet. Create products and they'll appear here.</p>
          ) : (
            <div className="overflow-x-auto">
              <table className="w-full text-left text-sm">
                <thead>
                  <tr className="border-b border-border">
                    <th className="py-3 px-4 font-medium text-muted-foreground">Product ID</th>
                    <th className="py-3 px-4 font-medium text-muted-foreground">Available</th>
                    <th className="py-3 px-4 font-medium text-muted-foreground">Reserved</th>
                    <th className="py-3 px-4 font-medium text-muted-foreground">Status</th>
                  </tr>
                </thead>
                <tbody>
                  {stockItems.map((item) => {
                    const status = getStockStatus(item.available)
                    const StatusIcon = status.icon
                    return (
                      <tr key={item.id} className="border-b border-border/50 hover:bg-secondary/30 transition-colors">
                        <td className="py-3 px-4">
                          <span className="font-semibold">#{item.productId}</span>
                        </td>
                        <td className="py-3 px-4">
                          <span className={`font-mono font-semibold ${status.textClass}`}>{item.available}</span>
                        </td>
                        <td className="py-3 px-4">
                          <span className="font-mono text-muted-foreground">{item.reserved}</span>
                        </td>
                        <td className="py-3 px-4">
                          <span className={`inline-flex items-center gap-1 rounded-full border px-2 py-0.5 text-xs font-medium ${status.className}`}>
                            <StatusIcon className="h-3 w-3" />
                            {status.label}
                          </span>
                        </td>
                      </tr>
                    )
                  })}
                </tbody>
              </table>
            </div>
          )}
        </CardContent>
      </Card>
    </main>
  )
}
