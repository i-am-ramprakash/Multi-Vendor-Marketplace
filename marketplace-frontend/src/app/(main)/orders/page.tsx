'use client';

import { Package, Eye } from 'lucide-react';
import Link from 'next/link';
import { formatCurrency, formatDate, getStatusColor } from '@/lib/utils';

export default function OrdersPage() {
  const orders = [
    {
      id: 1,
      orderNumber: 'ORD-001',
      status: 'DELIVERED',
      total: 179.98,
      items: 2,
      date: '2024-03-15',
    },
    {
      id: 2,
      orderNumber: 'ORD-002',
      status: 'SHIPPED',
      total: 99.99,
      items: 1,
      date: '2024-03-20',
    },
    {
      id: 3,
      orderNumber: 'ORD-003',
      status: 'PROCESSING',
      total: 249.97,
      items: 3,
      date: '2024-03-22',
    },
  ];

  return (
    <div className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8 py-8">
      <h1 className="text-3xl font-bold mb-8">My Orders</h1>

      {orders.length === 0 ? (
        <div className="text-center py-16">
          <Package className="h-16 w-16 text-gray-300 mx-auto mb-4" />
          <h2 className="text-xl font-semibold mb-2">No orders yet</h2>
          <p className="text-gray-600 mb-6">Start shopping to place your first order</p>
          <Link
            href="/products"
            className="bg-primary text-white px-6 py-3 rounded-lg font-semibold hover:bg-primary/90 inline-block"
          >
            Browse Products
          </Link>
        </div>
      ) : (
        <div className="bg-white rounded-lg shadow-sm">
          <div className="p-6 border-b">
            <div className="grid grid-cols-6 gap-4 text-sm font-medium text-gray-600">
              <div>Order</div>
              <div>Date</div>
              <div>Items</div>
              <div>Total</div>
              <div>Status</div>
              <div>Actions</div>
            </div>
          </div>
          {orders.map((order) => (
            <div key={order.id} className="p-6 border-b last:border-b-0">
              <div className="grid grid-cols-6 gap-4 items-center">
                <div className="font-semibold">{order.orderNumber}</div>
                <div className="text-gray-600">{formatDate(order.date)}</div>
                <div className="text-gray-600">{order.items} items</div>
                <div className="font-semibold">{formatCurrency(order.total)}</div>
                <div>
                  <span className={`px-3 py-1 rounded-full text-xs font-medium ${getStatusColor(order.status)}`}>
                    {order.status}
                  </span>
                </div>
                <div>
                  <Link
                    href={`/orders/${order.id}`}
                    className="text-primary hover:underline inline-flex items-center"
                  >
                    <Eye className="h-4 w-4 mr-1" />
                    View
                  </Link>
                </div>
              </div>
            </div>
          ))}
        </div>
      )}
    </div>
  );
}
