'use client';

import { Eye, Package } from 'lucide-react';
import { formatCurrency, formatDate, getStatusColor } from '@/lib/utils';

export default function VendorOrdersPage() {
  const orders = [
    { id: 'ORD-001', customer: 'John Doe', total: 179.98, items: 2, status: 'PENDING', date: '2024-03-15' },
    { id: 'ORD-002', customer: 'Jane Smith', total: 99.99, items: 1, status: 'SHIPPED', date: '2024-03-20' },
    { id: 'ORD-003', customer: 'Bob Johnson', total: 249.97, items: 3, status: 'DELIVERED', date: '2024-03-22' },
  ];

  return (
    <div>
      <h1 className="text-3xl font-bold mb-8">Orders</h1>

      <div className="bg-white rounded-lg shadow-sm">
        <div className="p-6 border-b">
          <div className="grid grid-cols-6 gap-4 text-sm font-medium text-gray-600">
            <div>Order</div>
            <div>Customer</div>
            <div>Date</div>
            <div>Items</div>
            <div>Total</div>
            <div>Status</div>
          </div>
        </div>
        {orders.map((order) => (
          <div key={order.id} className="p-6 border-b last:border-b-0">
            <div className="grid grid-cols-6 gap-4 items-center">
              <div className="font-semibold">{order.id}</div>
              <div>{order.customer}</div>
              <div className="text-gray-600">{formatDate(order.date)}</div>
              <div>{order.items} items</div>
              <div className="font-semibold">{formatCurrency(order.total)}</div>
              <div>
                <span className={`px-3 py-1 rounded-full text-xs font-medium ${getStatusColor(order.status)}`}>
                  {order.status}
                </span>
              </div>
            </div>
          </div>
        ))}
      </div>
    </div>
  );
}
