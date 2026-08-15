'use client';

import { Download, FileText, TrendingUp, Users, Package } from 'lucide-react';
import { formatCurrency } from '@/lib/utils';

export default function AdminReportsPage() {
  const reports = [
    { name: 'Sales Report', description: 'Detailed sales analytics and trends', icon: TrendingUp, lastGenerated: '2024-03-25' },
    { name: 'User Report', description: 'User registrations and activity', icon: Users, lastGenerated: '2024-03-24' },
    { name: 'Product Report', description: 'Product catalog and performance', icon: Package, lastGenerated: '2024-03-23' },
    { name: 'Commission Report', description: 'Commission calculations and payouts', icon: FileText, lastGenerated: '2024-03-22' },
  ];

  const summaryStats = [
    { label: 'Total Revenue', value: formatCurrency(245000) },
    { label: 'Total Orders', value: '8,900' },
    { label: 'Average Order Value', value: formatCurrency(27.53) },
    { label: 'Commission Earned', value: formatCurrency(24500) },
  ];

  return (
    <div>
      <h1 className="text-3xl font-bold mb-8">Reports</h1>

      <div className="grid grid-cols-1 md:grid-cols-4 gap-6 mb-8">
        {summaryStats.map((stat) => (
          <div key={stat.label} className="bg-white rounded-lg shadow-sm p-6">
            <p className="text-sm text-gray-600">{stat.label}</p>
            <p className="text-2xl font-bold mt-1">{stat.value}</p>
          </div>
        ))}
      </div>

      <div className="grid grid-cols-1 md:grid-cols-2 gap-6">
        {reports.map((report) => {
          const Icon = report.icon;
          return (
            <div key={report.name} className="bg-white rounded-lg shadow-sm p-6">
              <div className="flex items-start">
                <div className="bg-primary/10 p-3 rounded-lg">
                  <Icon className="h-6 w-6 text-primary" />
                </div>
                <div className="ml-4 flex-1">
                  <h3 className="font-semibold">{report.name}</h3>
                  <p className="text-sm text-gray-600">{report.description}</p>
                  <p className="text-xs text-gray-500 mt-2">Last generated: {report.lastGenerated}</p>
                </div>
                <button className="flex items-center text-primary hover:text-primary/80">
                  <Download className="h-5 w-5 mr-1" />
                  Export
                </button>
              </div>
            </div>
          );
        })}
      </div>
    </div>
  );
}
