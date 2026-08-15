'use client';

import Link from 'next/link';
import { usePathname } from 'next/navigation';
import {
  LayoutDashboard,
  Users,
  Package,
  DollarSign,
  UserCheck,
  FileText,
} from 'lucide-react';

const adminLinks = [
  { href: '/admin/dashboard', label: 'Dashboard', icon: LayoutDashboard },
  { href: '/admin/vendors', label: 'Vendor Management', icon: Users },
  { href: '/admin/products', label: 'Product Moderation', icon: Package },
  { href: '/admin/commissions', label: 'Commission Rules', icon: DollarSign },
  { href: '/admin/users', label: 'User Management', icon: UserCheck },
  { href: '/admin/reports', label: 'Reports', icon: FileText },
];

export default function AdminLayout({ children }: { children: React.ReactNode }) {
  const pathname = usePathname();

  return (
    <div className="flex min-h-screen bg-gray-50">
      {/* Sidebar */}
      <aside className="w-64 bg-gray-900 text-white">
        <div className="p-6">
          <h2 className="text-xl font-bold">Admin Panel</h2>
        </div>
        <nav className="px-4">
          {adminLinks.map((link) => {
            const Icon = link.icon;
            const isActive = pathname === link.href;
            return (
              <Link
                key={link.href}
                href={link.href}
                className={`flex items-center px-4 py-3 mb-2 rounded-lg ${
                  isActive ? 'bg-primary text-white' : 'text-gray-300 hover:bg-gray-800'
                }`}
              >
                <Icon className="h-5 w-5 mr-3" />
                {link.label}
              </Link>
            );
          })}
        </nav>
      </aside>

      {/* Main Content */}
      <main className="flex-1 p-8">{children}</main>
    </div>
  );
}
