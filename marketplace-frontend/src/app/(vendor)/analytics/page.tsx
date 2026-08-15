'use client';

import { TrendingUp, DollarSign, ShoppingCart, Users } from 'lucide-react';
import { formatCurrency } from '@/lib/utils';

export default function VendorAnalyticsPage() {
  const stats = [
    { label: 'Revenue (This Month)', value: '$4,500', change: '+15%', icon: DollarSign },
    { label: 'Orders (This Month)', value: '52', change: '+10%', icon: ShoppingCart },
    { label: 'Conversion Rate', value: '3.2%', change: '+0.5%', icon: TrendingUp },
    { label: 'Returning Customers', value: '28%', change: '+5%', icon: Users },
  ];

  const topProducts = [
    { name: 'Wireless Headphones', sales: 45, revenue: 4499.55 },
    { name: 'Running Shoes', sales: 32, revenue: 2559.68 },
    { name: 'Smart Watch', sales: 18, revenue: 3599.82 },
  ];

  return (
    <div>
      <h1 className="text-3xl font-bold mb-8">Analytics</h1>

      <div className="grid grid-cols-1 md:grid-cols-4 gap-6 mb-8">
        {stats.map((stat) => {
          const Icon = stat.icon;
          return (
            <div key={stat.label} className="bg-white rounded-lg shadow-sm p-6">
              <div className="flex items-center">
                <Icon className="h-8 w-8 text-primary" />
                <div className="ml-4">
                  <p className="text-sm text-gray-600">{stat.label}</p>
                  <p className="text-2xl font-bold">{stat.value}</p>
                  <p className="text-sm text-green-600">{stat.change}</p>
                </div>
              </div>
            </div>
          );
        })}
      </div>

      <div className="grid grid-cols-1 lg:grid-cols-2 gap-8">
        <div className="bg-white rounded-lg shadow-sm p-6">
          <h2 className="text-xl font-semibold mb-4">Top Products</h2>
          <div className="space-y-4">
            {topProducts.map((product, index) => (
              <div key={index} className="flex items-center justify-between p-3 bg-gray-50 rounded-lg">
                <div>
                  <p className="font-medium">{product.name}</p>
                  <p className="text-sm text-gray-600">{product.sales} sales</p>
                </div>
                <p className="font-semibold">{formatCurrency(product.revenue)}</p>
              </div>
            ))}
          </div>
        </div>

        <div className="bg-white rounded-lg shadow-sm p-6">
          <h2 className="text-xl font-semibold mb-4">Sales Trend</h2>
          <div className="h-64 flex items-center justify-center text-gray-400">
            Chart placeholder
          </div>
        </div>
      </div>
    </div>
  );
}
