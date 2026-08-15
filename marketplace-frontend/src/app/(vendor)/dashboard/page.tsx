'use client';

import { DollarSign, ShoppingCart, Package, TrendingUp } from 'lucide-react';
import { formatCurrency } from '@/lib/utils';

export default function VendorDashboardPage() {
  const stats = [
    { label: 'Total Sales', value: '$12,450', change: '+12%', icon: DollarSign, color: 'bg-green-500' },
    { label: 'Total Orders', value: '156', change: '+8%', icon: ShoppingCart, color: 'bg-blue-500' },
    { label: 'Products', value: '24', change: '+2', icon: Package, color: 'bg-purple-500' },
    { label: 'Conversion Rate', value: '3.2%', change: '+0.5%', icon: TrendingUp, color: 'bg-orange-500' },
  ];

  const recentOrders = [
    { id: 'ORD-001', customer: 'John Doe', total: 179.98, status: 'PENDING' },
    { id: 'ORD-002', customer: 'Jane Smith', total: 99.99, status: 'SHIPPED' },
    { id: 'ORD-003', customer: 'Bob Johnson', total: 249.97, status: 'DELIVERED' },
  ];

  return (
    <div>
      <h1 className="text-3xl font-bold mb-8">Vendor Dashboard</h1>

      <div className="grid grid-cols-1 md:grid-cols-4 gap-6 mb-8">
        {stats.map((stat) => {
          const Icon = stat.icon;
          return (
            <div key={stat.label} className="bg-white rounded-lg shadow-sm p-6">
              <div className="flex items-center">
                <div className={`${stat.color} p-3 rounded-lg`}>
                  <Icon className="h-6 w-6 text-white" />
                </div>
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

      <div className="bg-white rounded-lg shadow-sm p-6">
        <h2 className="text-xl font-semibold mb-4">Recent Orders</h2>
        <div className="overflow-x-auto">
          <table className="w-full">
            <thead>
              <tr className="border-b">
                <th className="text-left py-3 px-4">Order</th>
                <th className="text-left py-3 px-4">Customer</th>
                <th className="text-left py-3 px-4">Total</th>
                <th className="text-left py-3 px-4">Status</th>
              </tr>
            </thead>
            <tbody>
              {recentOrders.map((order) => (
                <tr key={order.id} className="border-b last:border-b-0">
                  <td className="py-3 px-4 font-medium">{order.id}</td>
                  <td className="py-3 px-4">{order.customer}</td>
                  <td className="py-3 px-4">{formatCurrency(order.total)}</td>
                  <td className="py-3 px-4">
                    <span className={`px-2 py-1 rounded-full text-xs ${
                      order.status === 'PENDING' ? 'bg-yellow-100 text-yellow-800' :
                      order.status === 'SHIPPED' ? 'bg-blue-100 text-blue-800' :
                      'bg-green-100 text-green-800'
                    }`}>
                      {order.status}
                    </span>
                  </td>
                </tr>
              ))}
            </tbody>
          </table>
        </div>
      </div>
    </div>
  );
}
