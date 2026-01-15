// features/orders/pages/OrdersPage.jsx
// Orders page with create + list + reserve stock functionality

import { useState } from 'react'
import { RefreshCw, Plus, X, ShoppingCart, Package, CheckCircle, XCircle } from 'lucide-react'
import { useOrders } from '../hooks/useOrders'
import { useProducts } from '../../products/hooks/useProducts'
import { Button, Card, CardContent, CardHeader, CardTitle, Input, Label } from '../../../shared/ui'

export default function OrdersPage() {
  const { items: orders, errorMessage, busy, count, refresh, create, reserve, updateStatus } = useOrders()
  const { items: products } = useProducts()

  // Form state
  const [customerName, setCustomerName] = useState('')
  const [orderItems, setOrderItems] = useState([{ productId: '', quantity: 1 }])

  const addItem = () => {
    setOrderItems([...orderItems, { productId: '', quantity: 1 }])
  }

  const removeItem = (index) => {
    setOrderItems(orderItems.filter((_, i) => i !== index))
  }

  const updateItem = (index, field, value) => {
    const updated = [...orderItems]
    updated[index] = { ...updated[index], [field]: value }
    setOrderItems(updated)
  }

  const canSubmit = customerName.trim() && orderItems.some((i) => i.productId && i.quantity > 0)

  const handleSubmit = async (e) => {
    e.preventDefault()
    const validItems = orderItems
      .filter((i) => i.productId && i.quantity > 0)
      .map((i) => {
        const product = products.find((p) => p.id === Number(i.productId))
        return {
          productId: Number(i.productId),
          quantity: Number(i.quantity),
          unitPrice: product?.price || 0,
        }
      })

    await create({
      customerName,
      items: validItems,
    })

    // Reset form
    setCustomerName('')
    setOrderItems([{ productId: '', quantity: 1 }])
    refresh()
  }

  const getStatusStyle = (status) => {
    switch (status) {
      case 'CREATED':
        return 'bg-blue-500/20 text-blue-300 border-blue-500/30'
      case 'RESERVED':
        return 'bg-green-500/20 text-green-300 border-green-500/30'
      case 'CANCELLED':
        return 'bg-destructive/20 text-destructive border-destructive/30'
      case 'COMPLETED':
        return 'bg-purple-500/20 text-purple-300 border-purple-500/30'
      default:
        return 'bg-secondary text-muted-foreground border-border'
    }
  }

  const formatPrice = (price) => (price != null ? Number(price).toFixed(2) : '')
  const formatDate = (dateStr) => (dateStr ? new Date(dateStr).toLocaleString() : '')

  return (
    <main className="mx-auto w-full max-w-5xl px-6 py-10">
      <header className="mb-8">
        <div className="flex items-center gap-3">
          <ShoppingCart className="h-8 w-8 text-muted-foreground" />
          <h1 className="text-3xl font-bold tracking-tight">Orders</h1>
          <span className="rounded-full bg-secondary px-2.5 py-0.5 text-xs font-medium text-muted-foreground">
            {count} orders
          </span>
        </div>
        <p className="mt-2 text-muted-foreground">Create orders, reserve stock (via gRPC), and manage lifecycle</p>
      </header>

      {/* Error message */}
      {errorMessage && (
        <p className="mb-4 rounded-lg border border-destructive/30 bg-destructive/10 p-3 text-sm text-destructive">
          {errorMessage}
        </p>
      )}

      <div className="grid gap-6 lg:grid-cols-2">
        {/* CREATE ORDER FORM */}
        <Card>
          <CardHeader>
            <CardTitle>Create Order</CardTitle>
          </CardHeader>
          <CardContent>
            <form onSubmit={handleSubmit} className="space-y-4">
              <div className="grid gap-2">
                <Label htmlFor="customerName">Customer Name</Label>
                <Input
                  id="customerName"
                  value={customerName}
                  onChange={(e) => setCustomerName(e.target.value)}
                  placeholder="John Doe"
                />
              </div>

              <div className="space-y-3">
                <div className="flex items-center justify-between">
                  <Label>Order Items</Label>
                  <Button type="button" variant="outline" size="sm" onClick={addItem}>
                    <Plus className="mr-1 h-3 w-3" />
                    Add Item
                  </Button>
                </div>

                {orderItems.map((item, idx) => (
                  <div key={idx} className="flex gap-2 items-end">
                    <div className="grid gap-2 flex-1">
                      <Label className="text-xs text-muted-foreground">Product</Label>
                      <select
                        value={item.productId}
                        onChange={(e) => updateItem(idx, 'productId', e.target.value)}
                        className="flex h-10 w-full rounded-md border border-input bg-background px-3 py-2 text-sm ring-offset-background focus-visible:outline-none focus-visible:ring-2 focus-visible:ring-ring"
                      >
                        <option value="">Select product</option>
                        {products.map((p) => (
                          <option key={p.id} value={p.id}>
                            {p.name} (${formatPrice(p.price)})
                          </option>
                        ))}
                      </select>
                    </div>

                    <div className="grid gap-2 w-24">
                      <Label className="text-xs text-muted-foreground">Qty</Label>
                      <Input
                        type="number"
                        min="1"
                        value={item.quantity}
                        onChange={(e) => updateItem(idx, 'quantity', e.target.value)}
                      />
                    </div>

                    {orderItems.length > 1 && (
                      <Button type="button" variant="destructive" size="icon" onClick={() => removeItem(idx)}>
                        <X className="h-4 w-4" />
                      </Button>
                    )}
                  </div>
                ))}
              </div>

              <Button type="submit" disabled={!canSubmit || busy} className="w-full">
                {busy ? 'Creating…' : 'Create Order'}
              </Button>
            </form>
          </CardContent>
        </Card>

        {/* ORDERS LIST */}
        <Card>
          <CardHeader className="flex flex-row items-center justify-between">
            <CardTitle>Orders List</CardTitle>
            <Button variant="outline" size="sm" onClick={refresh} disabled={busy}>
              <RefreshCw className={`mr-1 h-3 w-3 ${busy ? 'animate-spin' : ''}`} />
              Refresh
            </Button>
          </CardHeader>
          <CardContent>
            {orders.length === 0 ? (
              <p className="text-sm text-muted-foreground">No orders yet.</p>
            ) : (
              <div className="space-y-4 max-h-[500px] overflow-y-auto pr-2">
                {orders.map((order) => (
                  <div key={order.id} className="rounded-lg border border-border/50 bg-card/50 p-4">
                    <div className="flex items-start justify-between gap-3">
                      <div className="min-w-0">
                        <div className="flex flex-wrap items-center gap-2">
                          <span className="font-semibold">Order #{order.id}</span>
                          <span className={`rounded-full border px-2 py-0.5 text-xs font-medium ${getStatusStyle(order.status)}`}>
                            {order.status}
                          </span>
                        </div>
                        <p className="mt-1 text-sm text-muted-foreground">{order.customerName}</p>
                        <p className="text-xs text-muted-foreground">{formatDate(order.createdAt)}</p>
                      </div>
                      <p className="text-sm font-semibold shrink-0">${formatPrice(order.total)}</p>
                    </div>

                    {/* Items */}
                    {order.items?.length > 0 && (
                      <div className="mt-3 flex items-center gap-1 text-xs text-muted-foreground">
                        <Package className="h-3 w-3" />
                        {order.items.map((item, i) => (
                          <span key={item.id}>
                            {item.quantity}x Product#{item.productId}
                            {i < order.items.length - 1 ? ', ' : ''}
                          </span>
                        ))}
                      </div>
                    )}

                    {/* Actions */}
                    <div className="mt-3 flex flex-wrap gap-2">
                      {order.status === 'CREATED' && (
                        <Button
                          size="sm"
                          variant="outline"
                          disabled={busy}
                          onClick={() => reserve(order.id).then(refresh)}
                          className="border-green-500/30 bg-green-500/10 text-green-300 hover:bg-green-500/20"
                        >
                          <CheckCircle className="mr-1 h-3 w-3" />
                          Reserve Stock
                        </Button>
                      )}
                      {order.status === 'RESERVED' && (
                        <Button
                          size="sm"
                          variant="outline"
                          disabled={busy}
                          onClick={() => updateStatus(order.id, 'COMPLETED').then(refresh)}
                          className="border-purple-500/30 bg-purple-500/10 text-purple-300 hover:bg-purple-500/20"
                        >
                          Complete
                        </Button>
                      )}
                      {order.status !== 'CANCELLED' && order.status !== 'COMPLETED' && (
                        <Button
                          size="sm"
                          variant="outline"
                          disabled={busy}
                          onClick={() => updateStatus(order.id, 'CANCELLED').then(refresh)}
                          className="border-destructive/30 bg-destructive/10 text-destructive hover:bg-destructive/20"
                        >
                          <XCircle className="mr-1 h-3 w-3" />
                          Cancel
                        </Button>
                      )}
                    </div>
                  </div>
                ))}
              </div>
            )}
          </CardContent>
        </Card>
      </div>
    </main>
  )
}
