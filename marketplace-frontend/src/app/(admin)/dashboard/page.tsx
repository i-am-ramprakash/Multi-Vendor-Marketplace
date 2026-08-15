'use client';

import { Users, Package, ShoppingCart, DollarSign, TrendingUp, AlertTriangle } from 'lucide-react';
import { formatCurrency } from '@/lib/utils';

export default function AdminDashboardPage() {
  const stats = [
    { label: 'Total Users', value: '12,450', change: '+12%', icon: Users, color: 'bg-blue-500' },
    { label: 'Active Vendors', value: '156', change: '+8%', icon: Users, color: 'bg-green-500' },
    { label: 'Total Products', value: '2,340', change: '+15%', icon: Package, color: 'bg-purple-500' },
    { label: 'Total Orders', value: '8,900', change: '+10%', icon: ShoppingCart, color: 'bg-orange-500' },
    { label: 'Total Revenue', value: '$245,000', change: '+18%', icon: DollarSign, color: 'bg-green-500' },
    { label: 'Pending Approvals', value: '23', change: '-5%', icon: AlertTriangle, color: 'bg-yellow-500' },
  ];

  const recentActivity = [
    { type: 'vendor', message: 'New vendor registration: TechStore', time: '2 hours ago' },
    { type: 'product', message: 'Product pending approval: Wireless Headphones', time: '3 hours ago' },
    { type: 'order', message: 'Large order placed: $1,500', time: '5 hours ago' },
    { type: 'vendor', message: 'Vendor store updated: FashionHub', time: '6 hours ago' },
  ];

  return (
    <div>
      <h1 className="text-3xl font-bold mb-8">Admin Dashboard</h1>

      <div className="grid grid-cols-1 md:grid-cols-3 gap-6 mb-8">
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

      <div className="grid grid-cols-1 lg:grid-cols-2 gap-8">
        <div className="bg-white rounded-lg shadow-sm p-6">
          <h2 className="text-xl font-semibold mb-4">Recent Activity</h2>
          <div className="space-y-4">
            {recentActivity.map((activity, index) => (
              <div key={index} className="flex items-start p-3 bg-gray-50 rounded-lg">
                <div className={`w-2 h-2 rounded-full mt-2 mr-3 ${
                  activity.type === 'vendor' ? 'bg-blue-500' :
                  activity.type === 'product' ? 'bg-purple-500' :
                  'bg-green-500'
                }`} />
                <div>
                  <p className="text-sm">{activity.message}</p>
                  <p className="text-xs text-gray-500">{activity.time}</p>
                </div>
              </div>
            ))}
          </div>
        </div>

        <div className="bg-white rounded-lg shadow-sm p-6">
          <h2 className="text-xl font-semibold mb-4">Revenue Overview</h2>
          <div className="h-64 flex items-center justify-center text-gray-400">
            Chart placeholder
          </div>
        </div>
      </div>
    </div>
  );
}
