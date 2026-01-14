// features/products/pages/ProductsPage.jsx
// Main Products page with full CRUD (mirrors Vue ProductsPage.vue)

import { RefreshCw } from 'lucide-react'
import ProductForm from '../components/ProductForm'
import ProductList from '../components/ProductList'
import { useProducts } from '../hooks/useProducts'
import { Button } from '../../../shared/ui'

export default function ProductsPage() {
  const {
    items: products,
    errorMessage,
    busy,
    count,
    editingProduct,
    refresh,
    save,
    remove,
    startEdit,
    cancelEdit,
  } = useProducts()

  return (
    <main className="mx-auto w-full max-w-5xl px-6 py-10">
      <header className="mb-8">
        <div className="flex items-center gap-3">
          <h1 className="text-3xl font-bold tracking-tight">Products</h1>
          <span className="rounded-full bg-secondary px-2.5 py-0.5 text-xs font-medium text-muted-foreground">
            {count} items
          </span>
        </div>
        <p className="mt-2 text-muted-foreground">
          Create, read, update and delete products from your Spring Boot backend
        </p>
      </header>

      {/* Error message */}
      {errorMessage && (
        <p className="mb-4 rounded-lg border border-destructive/30 bg-destructive/10 p-3 text-sm text-destructive">
          {errorMessage}
        </p>
      )}

      {busy && !products.length && (
        <p className="mb-4 text-sm text-muted-foreground">Loading…</p>
      )}

      <div className="grid grid-cols-1 gap-6 md:grid-cols-2">
        {/* CREATE + UPDATE */}
        <ProductForm
          initialProduct={editingProduct}
          busy={busy}
          onSave={save}
          onCancel={cancelEdit}
        />

        {/* READ + DELETE + choose product to edit */}
        <ProductList
          products={products}
          busy={busy}
          onEdit={startEdit}
          onDelete={remove}
        />
      </div>

      <div className="mt-6 flex justify-end">
        <Button variant="outline" disabled={busy} onClick={refresh}>
          <RefreshCw className={`mr-2 h-4 w-4 ${busy ? 'animate-spin' : ''}`} />
          Refresh
        </Button>
      </div>
    </main>
  )
}
