// features/products/pages/ProductsPage.jsx
// Main Products page with full CRUD (mirrors Vue ProductsPage.vue)

import { RefreshCw } from 'lucide-react'
import ProductForm from '../components/ProductForm'
import ProductList from '../components/ProductList'
import { useProducts } from '../hooks/useProducts'
import { Button, Card, CardContent, CardHeader, CardTitle, Input, Label } from '../../../shared/ui'
import { useAuth } from '../../auth/hooks/useAuth'

export default function ProductsPage() {
  const auth = useAuth()
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

      {/* Auth (JWT) */}
      <Card className="mb-6">
        <CardHeader>
          <CardTitle>Authentication</CardTitle>
        </CardHeader>
        <CardContent>
          {auth.errorMessage && (
            <p className="mb-3 rounded-lg border border-destructive/30 bg-destructive/10 p-3 text-sm text-destructive">
              {auth.errorMessage}
            </p>
          )}

          {auth.isAuthenticated ? (
            <div className="flex flex-col gap-3 sm:flex-row sm:items-center sm:justify-between">
              <div className="text-sm text-muted-foreground">
                Logged in as{' '}
                <span className="font-medium text-foreground">{auth.username || 'user'}</span>
                {auth.role ? <span className="text-muted-foreground"> ({auth.role})</span> : null}
              </div>
              <Button variant="outline" onClick={auth.logout}>
                Logout
              </Button>
            </div>
          ) : (
            <div className="grid gap-4 sm:grid-cols-3 sm:items-end">
              <div className="grid gap-2">
                <Label htmlFor="login-username">Username</Label>
                <Input
                  id="login-username"
                  value={auth.form.username}
                  onChange={(e) => auth.onChange('username', e.target.value)}
                  placeholder="admin"
                  autoComplete="username"
                />
              </div>
              <div className="grid gap-2">
                <Label htmlFor="login-password">Password</Label>
                <Input
                  id="login-password"
                  type="password"
                  value={auth.form.password}
                  onChange={(e) => auth.onChange('password', e.target.value)}
                  placeholder="admin123"
                  autoComplete="current-password"
                />
              </div>
              <Button disabled={auth.loading} onClick={auth.login}>
                {auth.loading ? 'Signing in…' : 'Login'}
              </Button>
            </div>
          )}

          <p className="mt-3 text-xs text-muted-foreground">
            Creating, updating, and deleting products requires a JWT.
          </p>
        </CardContent>
      </Card>

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
