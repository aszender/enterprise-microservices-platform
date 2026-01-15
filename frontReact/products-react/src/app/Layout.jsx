// app/Layout.jsx
// Root layout with navigation

import { Link, Outlet, useLocation } from 'react-router-dom'
import { Package, Home, Sparkles, ShoppingCart, Warehouse } from 'lucide-react'
import { cn } from '../shared/lib/utils'

const navItems = [
  { to: '/', label: 'Home', icon: Home },
  { to: '/products', label: 'Products', icon: Package },
  { to: '/orders', label: 'Orders', icon: ShoppingCart },
  { to: '/inventory', label: 'Inventory', icon: Warehouse },
]

export default function Layout() {
  const { pathname } = useLocation()

  return (
    <div className="min-h-screen bg-gradient-to-b from-background to-background/95">
      {/* Navigation */}
      <nav className="sticky top-0 z-50 border-b border-border/50 bg-background/80 backdrop-blur-xl">
        <div className="mx-auto flex max-w-5xl items-center justify-between px-6 py-4">
          {/* Logo */}
          <Link to="/" className="group flex items-center gap-2">
            <div className="flex h-8 w-8 items-center justify-center rounded-lg bg-gradient-to-br from-violet-500 to-purple-600 shadow-lg shadow-purple-500/20 transition-transform group-hover:scale-105">
              <Sparkles className="h-4 w-4 text-white" />
            </div>
            <span className="text-lg font-bold tracking-tight">Products React</span>
          </Link>

          {/* Nav links */}
          <div className="flex items-center gap-1">
            {navItems.map(({ to, label, icon: Icon }) => (
              <Link
                key={to}
                to={to}
                className={cn(
                  'flex items-center gap-2 rounded-lg px-4 py-2 text-sm font-medium transition-all',
                  pathname === to
                    ? 'bg-secondary text-foreground shadow-sm'
                    : 'text-muted-foreground hover:bg-secondary/50 hover:text-foreground'
                )}
              >
                <Icon className="h-4 w-4" />
                {label}
              </Link>
            ))}
          </div>
        </div>
      </nav>

      {/* Page content */}
      <Outlet />
    </div>
  )
}
