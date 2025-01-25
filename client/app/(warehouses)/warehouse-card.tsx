import WarehouseIcon from "@/components/icons/warehouse";
import { Card, CardContent, CardHeader, CardTitle } from "@/components/ui/card";
import Link from "next/link";

export default function WarehouseCard({
    id,
    name,
    image,
}: {
    id: string;
    name: string;
    image: string;
}) {
    return (
        <Card className="h-72 min-w-96">
            <Link href={`/${id}/products`}>
                <CardHeader className="text-center">
                    <CardTitle>{name}</CardTitle>
                </CardHeader>
                <CardContent className="flex justify-center">
                    <WarehouseIcon />
                </CardContent>
            </Link>
        </Card>
    );
}
